package com.grepp.datenow.app.controller.api.member;

import com.grepp.datenow.app.controller.web.member.payload.MemberUpdateRequest;
import com.grepp.datenow.app.controller.web.member.payload.SignupRequest;
import com.grepp.datenow.app.model.auth.code.Role;
import com.grepp.datenow.app.model.auth.domain.Principal;
import com.grepp.datenow.app.model.like.dto.FavoriteCourseResponse;
import com.grepp.datenow.app.model.like.service.FavoriteService;
import com.grepp.datenow.app.model.member.dto.MemberDto;
import com.grepp.datenow.app.model.member.entity.Member;
import com.grepp.datenow.app.model.member.repository.MemberRepository;
import com.grepp.datenow.app.model.member.service.MemberService;
import com.grepp.datenow.infra.response.ApiResponse;
import com.grepp.datenow.infra.response.ResponseCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/members")
public class MemberApiController {

    private final MemberService memberService;
    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    private final FavoriteService favoriteService;


    @GetMapping("/exists/userId")
    public ResponseEntity<ApiResponse<Boolean>> checkUserId(@RequestParam String userId) {
        boolean exists = memberRepository.existsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(exists));
    }

    @GetMapping("/check/email")
    public ResponseEntity<ApiResponse<Boolean>> checkEmail(@RequestParam String email) {
        boolean exists = memberRepository.existsByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(exists));
    }

    @GetMapping("/check/nickname")
    public ResponseEntity<ApiResponse<Boolean>> checkNickname(@RequestParam String nickname) {
        boolean exists = memberRepository.existsByNickname(nickname);
        return ResponseEntity.ok(ApiResponse.success(exists));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<?>> signup(@RequestBody @Valid SignupRequest form) {

        MemberDto dto = modelMapper.map(form, MemberDto.class);
        memberService.signup(dto, Role.ROLE_USER);

        return ResponseEntity
            .status(ResponseCode.OK.status())
            .body(ApiResponse.success(Map.of("message", "회원가입이 완료되었습니다.")));
    }

    @PutMapping("/edit/{user_id}")
    public ResponseEntity<ApiResponse<?>> updateMember(
        @PathVariable("user_id") String userId,
        @RequestBody MemberUpdateRequest request) {

        memberService.updateMember(userId, request);
        return ResponseEntity
            .status(ResponseCode.OK.status())
            .body(ApiResponse.success(Map.of("message", "회원정보가 성공적으로 수정되었습니다.")));
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteCourseResponse>> getMyFavorites(
        @AuthenticationPrincipal Principal principal
    ) {
        String userId = principal.getUsername();
        List<FavoriteCourseResponse> favorites = favoriteService.getFavoriteCourses(userId);

        return ResponseEntity.ok(favorites);
    }

    @PatchMapping("/favorites/{favoriteCourseId}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long favoriteCourseId) {
        favoriteService.deactivateFavorite(favoriteCourseId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/deactivate")
    public ResponseEntity<Void> deactivateMember(
        @AuthenticationPrincipal Principal principal,
        HttpServletRequest request,
        HttpServletResponse response) throws ServletException {

        String userId = principal.getUsername();
        memberService.deactivateMember(userId);

        request.logout();
        return ResponseEntity.ok().build();
    }

    // 회원 정보 수정 페이지 데이터 전달 + 마이페이지 사이드바
    @GetMapping("/info")
    public ResponseEntity<MemberDto> getMemberInfo(@AuthenticationPrincipal Principal principal) {
        String userId = principal.getUsername();
        Member member = memberService.findByUserId(userId);
        MemberDto dto = MemberDto.from(member);

        return ResponseEntity.ok(dto);
    }
}
