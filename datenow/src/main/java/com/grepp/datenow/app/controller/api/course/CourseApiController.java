package com.grepp.datenow.app.controller.api.course;

import com.grepp.datenow.app.model.auth.domain.Principal;
import com.grepp.datenow.app.model.course.dto.CourseDetailDto;
import com.grepp.datenow.app.model.course.dto.MyCourseResponse;
import com.grepp.datenow.app.model.course.dto.MyDateCourseDto;
import com.grepp.datenow.app.model.course.dto.RecommendCourseRegistRequestDto;
import com.grepp.datenow.app.model.course.service.CourseService;
import com.grepp.datenow.app.model.image.service.ImageService;
import com.grepp.datenow.app.model.member.entity.Member;
import com.grepp.datenow.app.model.member.service.MemberService;
import com.grepp.datenow.infra.response.ApiResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
@Slf4j
public class CourseApiController {

    private final CourseService courseService;
    private final MemberService memberService;
    private final ImageService imageService;

    // 내가 만든 데이트 코스 등록
    @PostMapping("/save")
    public ResponseEntity<ApiResponse<?>> saveMyCourse(
        @RequestBody MyDateCourseDto dto,
        @AuthenticationPrincipal Principal principal
    ) {
        String userId = principal.getUsername();
        Member member = memberService.findByUserId(userId);

        courseService.saveCourse(dto, member);
        // 응답을 어떻게 처리할까.. 딱히 응답 없어도 되지 않나?
        return ResponseEntity.ok(ApiResponse.noContent());
    }

    // 내가 만든 데이트 코스 목록 조회 (마이페이지)
    @GetMapping("/my-course")
    public ResponseEntity<ApiResponse<List<MyCourseResponse>>> getMyCourses(
        @AuthenticationPrincipal Principal principal
    ) {
        String userId = principal.getUsername();

        Member member = memberService.findByUserId(userId);

        List<MyCourseResponse> courses = courseService.findMyCourses(member);
        return ResponseEntity.ok(ApiResponse.success(courses));
    }

    // 내가 만든 데이트 코스 상세정보 조회 (마이페이지)
    @GetMapping("/my-course/{id}")
    public ResponseEntity<ApiResponse<CourseDetailDto>> getCourseDetail(
        @PathVariable("id") Long courseId) {
        CourseDetailDto courseDetailDto = courseService.getCourseDetail(courseId);
        return ResponseEntity.ok(ApiResponse.success(courseDetailDto));
    }

    // 내가 만든 데이트 코스 추천 코스 등록 시 사진 업로드
    @PostMapping("/images")
    public ResponseEntity<ApiResponse<?>> uploadImages(
        @RequestParam("images") List<MultipartFile> images) {
        // Service 에서 모든 유효성 검사 및 예외 처리를 담당
        // 여기서 예외가 발생하면 GlobalExceptionHandler 가 처리
        List<String> urls = imageService.upload(images);

        // 성공 응답은 ApiResponse 포맷으로 반환
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(ApiResponse.success(urls));
//        .body(ApiResponse.success(urls, Map.of("message", "이미지 업로드가 완료되었습니다.")));
    }

    // 내가 만든 데이트 코스 추천 코스로 등록 (사진 제외)
    @PostMapping("/recommend-course-register")
    public ResponseEntity<?> recommendCourseRegister(
        @RequestBody RecommendCourseRegistRequestDto request
    ) {
        courseService.registerToRecommendCourse(request.getCourseId(), request.getImageUrls());

        return ResponseEntity.ok()
         .contentType(MediaType.APPLICATION_JSON)
         .body(ApiResponse.noContent());
    }

    // 나의 데이트 코스 상세 정보 조회
    // 이거 위에 상세조회랑 겹치는거 아닌가??
    @GetMapping("/recommend-course-register")
    public ResponseEntity<?> getMyCourseDetail(
        @RequestParam(required = false) Long courseId,
        @AuthenticationPrincipal Principal principal) {

        try {
            CourseDetailDto courseDetail = courseService.getCourseDetail(courseId);
            String userId = principal.getUsername();
            Member member = memberService.findByUserId(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("member", member);
            response.put("courseId", courseId);
            response.put("course", courseDetail);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error getting course detail", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal server error");
        }
    }
}