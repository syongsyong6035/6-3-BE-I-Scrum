package com.grepp.datenow.app.model.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import com.grepp.datenow.infra.error.exception.CommonException;
import com.grepp.datenow.infra.error.exception.member.NotExistEmailException;
import com.grepp.datenow.infra.error.exception.member.SessionExpiredException;
import com.grepp.datenow.infra.error.exception.member.TokenMismatchException;
import com.grepp.datenow.infra.event.Outbox;
import com.grepp.datenow.infra.mail.MailTemplate;
import com.grepp.datenow.infra.response.ResponseCode;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
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
    public void signup(MemberDto dto, Role role, HttpSession session) { // HttpSession 파라미터 추가
        log.info("회원가입 요청 시작: userId={}", dto.getUserId());

        if (memberRepository.existsByUserId(dto.getUserId())) {
            throw new CommonException(ResponseCode.BAD_REQUEST, "이미 사용 중인 아이디입니다.");
        }

        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new CommonException(ResponseCode.BAD_REQUEST, "이미 사용 중인 이메일입니다.");
        }

        if (memberRepository.existsByNickname(dto.getNickname())) {
            throw new CommonException(ResponseCode.BAD_REQUEST, "이미 사용 중인 닉네임입니다.");
        }

        // 비밀번호 인코딩 (dto.setPassword를 통해 dto에 인코딩된 비밀번호 저장)
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        dto.setRole(role); // dto에 Role 설정

        String verifyToken = session.getId(); // ⭐ verifyToken으로 session.getId() 사용

        // ⭐ 2. MemberDto를 HttpSession에 저장
        session.setAttribute("pending_signup_dto", dto); // 세션에 MemberDto 저장
        session.setAttribute("pending_signup_verify_token", verifyToken); // 세션에 verifyToken 저장 (만료 시간은 세션 만료 시간과 동기화)
        session.setAttribute("pending_signup_token_expired_at", LocalDateTime.now().plusMinutes(30)); // 세션 만료와 별개로 링크 유효 시간 기록
        log.info("임시 회원 정보 HttpSession에 저장됨: userId={}, sessionId={}, token={}", dto.getUserId(), session.getId(), verifyToken);

        // Outbox Payload 에 넣기 위해 야무지게 랩핑
        OutboxPayloadDto outboxPayload = OutboxPayloadDto.builder()
            .email(dto.getEmail())
            .verifyToken(verifyToken)
            .domain("http://localhost:8080")
            .build();

        // OutboxPayloadDto 를 JSON 문자열로 변환
        String payloadJson;
        try {
            payloadJson = objectMapper.writeValueAsString(outboxPayload);
        } catch (JsonProcessingException e) {
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
        log.info("인증 메일 발송 이벤트 Redis Pub/Sub에 발행됨: email={}", dto.getEmail());
    }

    @Transactional
    public void verifyEmail(String requestedToken, HttpSession session) {
        // 요청된 토큰과 세션 ID 일치 확인
        if (!session.getId().equals(requestedToken)) {
            throw new TokenMismatchException("비정상적인 접근입니다.");
        }

        // 세션에서 MemberDto 및 관련 정보 조회
        MemberDto pendingDto = (MemberDto) session.getAttribute("pending_signup_dto");
        String storedToken = (String) session.getAttribute("pending_signup_verify_token");
        LocalDateTime tokenExpiredAt = (LocalDateTime) session.getAttribute("pending_signup_token_expired_at");

        // 토큰 정보가 없거나 만료된 경우
        if (pendingDto == null || storedToken == null
            || tokenExpiredAt == null || LocalDateTime.now().isAfter(tokenExpiredAt)) {
            throw new SessionExpiredException("인증 정보가 만료되었거나 유효하지 않습니다.");
        }

        // 이미 가입된 아이디/이메일/닉네임인지 DB에 저장하기 전 다시 확인
        // 깔끔하게 하려면 임시 저장 회원 DB를 만들어야 할 거 같음.
        if (memberRepository.existsByUserId(pendingDto.getUserId())) {
            log.warn("인증하려는 아이디가 이미 존재함 (경쟁 조건): userId={}", pendingDto.getUserId());
            throw new CommonException(ResponseCode.BAD_REQUEST, "이미 사용 중인 아이디입니다.");
        }
        if (memberRepository.existsByEmail(pendingDto.getEmail())) {
            log.warn("인증하려는 이메일이 이미 존재함 (경쟁 조건): email={}", pendingDto.getEmail());
            throw new CommonException(ResponseCode.BAD_REQUEST, "이미 사용 중인 이메일입니다.");
        }
        if (memberRepository.existsByNickname(pendingDto.getNickname())) {
            log.warn("인증하려는 닉네임이 이미 존재함 (경쟁 조건): nickname={}", pendingDto.getNickname());
            throw new CommonException(ResponseCode.BAD_REQUEST, "이미 사용 중인 닉네임입니다.");
        }
        log.info("중복 아이디/이메일/닉네임 없음. 최종 DB 저장 준비.");

        // ⭐ 5. MemberDto를 Member 엔티티로 매핑 및 DB에 최종 저장
        Member member = mapper.map(pendingDto, Member.class); // MemberDto에서 Member로 매핑

        memberRepository.save(member); // DB에 새로운 레코드로 저장 (INSERT)
        log.info("Member 엔티티 DB에 최종 저장 성공: userId={}, email={}", member.getUserId(), member.getEmail());

        // ⭐ 6. HttpSession에서 임시 회원 정보 삭제
        session.removeAttribute("pending_signup_dto");
        session.removeAttribute("pending_signup_verify_token");
        session.removeAttribute("pending_signup_token_expired_at");
        log.info("HttpSession에서 임시 회원 정보 삭제됨: sessionId={}", session.getId());
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
            .orElseThrow(NotExistEmailException::new);

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
