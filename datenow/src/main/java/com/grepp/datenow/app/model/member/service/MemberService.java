package com.grepp.datenow.app.model.member.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grepp.datenow.app.controller.web.member.payload.MemberUpdateRequest;
import com.grepp.datenow.app.model.auth.code.Role;
import com.grepp.datenow.app.model.course.dto.MyCourseResponse;
import com.grepp.datenow.app.model.course.entity.Course;
import com.grepp.datenow.app.model.course.repository.MyCourseRepository;
import com.grepp.datenow.app.model.member.dto.MemberDto;
import com.grepp.datenow.app.model.member.dto.OutboxPayloadDto;
import com.grepp.datenow.app.model.member.entity.Member;
import com.grepp.datenow.app.model.member.repository.MemberRepository;
import com.grepp.datenow.infra.error.CommonException;
import com.grepp.datenow.infra.event.Outbox;
import com.grepp.datenow.infra.mail.MailTemplate;
import com.grepp.datenow.infra.response.ResponseCode;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final MyCourseRepository myCourseRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final MailTemplate mailTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Transactional
    public void signup(MemberDto dto, Role role) {

        if (memberRepository.existsByUserId(dto.getUserId())) {
            throw new CommonException(ResponseCode.BAD_REQUEST, "이미 사용 중인 아이디입니다.");
        }

        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new CommonException(ResponseCode.BAD_REQUEST, "이미 사용 중인 이메일입니다.");
        }

        if (memberRepository.existsByNickname(dto.getNickname())) {
            throw new CommonException(ResponseCode.BAD_REQUEST, "이미 사용 중인 닉네임입니다.");
        }

        Member member = mapper.map(dto, Member.class);

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        member.setPassword(encodedPassword);
        member.setRole(role);
        member.setVerified(false); // 아직 인증받기 전이니깐 false

        // Token 만들기
        String verifyToken = UUID.randomUUID().toString();
        member.setVerifyToken(verifyToken);
        // 30분 내에 인증 해야한다. 그것도 못하면 가입하지마
        member.setTokenExpiredAt(LocalDateTime.now().plusMinutes(30));

        memberRepository.save(member);

        OutboxPayloadDto outboxPayload = OutboxPayloadDto.builder()
            .email(member.getEmail())
            .verifyToken(verifyToken)
            .domain("http://localhost:8080")
            .build();

        // OutboxPayloadDto 를 JSON 문자열로 변환
        String payloadJson;
        try {
            payloadJson = objectMapper.writeValueAsString(outboxPayload);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            // 로깅 또는 예외 처리
            log.error("Failed to serialize OutboxPayloadDto to JSON", e);
            throw new CommonException(ResponseCode.INTERNAL_SERVER_ERROR, "Failed to prepare email data.");
        }

        Outbox outbox = Outbox.builder()
            .eventType("signup_verify")
            .payload(payloadJson)
            .sourceService("datenow")
            .build();

        // datenow 채널로 보내버리깅
        redisTemplate.convertAndSend("datenow", outbox);
    }

    @Transactional
    public Member findByUserId(String userId) {
        return memberRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
    }

    @Transactional
    public void updateMember(String userId, MemberUpdateRequest request) {
        Member member = findByUserId(userId);

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            member.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        member.setPhone(request.getPhone());
        member.setEmail(request.getEmail());
        member.setNickname(request.getNickname());

        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public List<MyCourseResponse> findMyCourses(Member member) {
        List<Course> courses = myCourseRepository.findById(member);

        return courses.stream()
            .map(c -> new MyCourseResponse(c.getCoursesId(), c.getTitle()))
            .collect(Collectors.toList());
    }

    @Transactional
  public String getNicknameByUserId(String name) {
      return memberRepository.findByUserId(name)
          .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."))
          .getNickname();
  }

    @Transactional
    public void deactivateMember(String userId) {
        Member member = memberRepository.findByUserId(userId)
            .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

        member.unActivated();
        member.setLeaved(true);
        member.setLeavedAt(LocalDateTime.now());
        memberRepository.save(member);
    }

    @Transactional
    public void processFindPassword(String email) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new CommonException(ResponseCode.BAD_REQUEST, "존재하지 않는 이메일입니다."));

        // 임시 비밀번호 생성
        String tempPassword = generateRandomPassword();
        member.setPassword(passwordEncoder.encode(tempPassword));
        memberRepository.save(member);

        // 메일 발송
        mailTemplate.setTo(email);
        mailTemplate.setTemplatePath("find_password_mail");
        mailTemplate.setSubject("DateNow 임시 비밀번호 발급");
        mailTemplate.setProperties("tempPassword", tempPassword);
        mailTemplate.send();
    }

    private String generateRandomPassword() {
        int length = 8;
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(length);
        java.security.SecureRandom rnd = new java.security.SecureRandom();
        for (int i = 0; i < length; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }
}
