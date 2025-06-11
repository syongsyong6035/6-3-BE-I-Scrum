package com.grepp.datenow.app.controller.api.member;

import com.grepp.datenow.app.model.course.dto.EditorCourseDto;
import com.grepp.datenow.app.model.member.dto.AdminSearchUserDto;
import com.grepp.datenow.app.model.member.service.AdminService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin") // Common Endpoint
@RequiredArgsConstructor
public class AdminApiController {

  private final AdminService adminService;

  // 회원 관리 페이지 전체 회원 목록 조회
  @GetMapping("/users")
  public ResponseEntity<?> userSearchAll(){
    List<AdminSearchUserDto> userAll = adminService.userAllSearch();
    return ResponseEntity.ok(userAll);
  }

  // 에디터픽 코스 목록 조회 (관리자용)
  @GetMapping("/recommend-courses")
  public ResponseEntity<?> adminCourseAll(){
    List<EditorCourseDto> editorCourses = adminService.adminAllCourse();
    return ResponseEntity.ok(editorCourses);
  }

  // 에디터픽 코스 삭제 (Soft Delete) > 그냥 Delete 로 바꿔야 하나?
  @PostMapping("/recommend-courses/{recommend_id}")
  public ResponseEntity<?> deleteCourse(@PathVariable Long recommend_id){
    adminService.adminRecommendDelete(recommend_id);
    return ResponseEntity.ok().body(Map.of("message","성공적으로 처리되었습니다"));
  }
}
