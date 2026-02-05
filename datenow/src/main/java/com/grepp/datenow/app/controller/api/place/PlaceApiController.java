package com.grepp.datenow.app.controller.api.place;

import com.grepp.datenow.app.model.auth.domain.Principal;
import com.grepp.datenow.app.model.course.dto.CourseDetailDto;
import com.grepp.datenow.app.model.course.dto.CourseDto;
import com.grepp.datenow.app.model.course.dto.EditorCourseDto;
import com.grepp.datenow.app.model.course.dto.EditorDetailCourseDto;
import com.grepp.datenow.app.model.course.service.CourseService;
import com.grepp.datenow.app.model.like.service.FavoriteService;
import com.grepp.datenow.app.model.place.dto.mainpage.AdminUserTopListDto;
import com.grepp.datenow.app.model.place.service.PlaceMainPageService;
import com.grepp.datenow.app.model.review.dto.RequestReviewDto;
import com.grepp.datenow.app.model.review.dto.ResponseReviewDto;
import com.grepp.datenow.app.model.review.service.ReviewService;
import com.grepp.datenow.infra.response.ApiResponse;
import com.grepp.datenow.infra.update.DummyDataService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PlaceApiController {

  // 관리자 기능, 유저 기능, 좋아요 기능 등등 좀 분리할 필요가 있지 않나?
  private final PlaceMainPageService placeMainPageService;
  private final ReviewService reviewService;
  private final FavoriteService favoriteService;
  private final CourseService courseService;
  private final DummyDataService dummyDataService;

  @GetMapping("/bulk-insert")
  public String triggerBulkInsert() {
    dummyDataService.bulkInsertForPerformanceTest();
    return "데이터 삽입 완료! DB를 확인해 보세요.";
  }

  // 메인페이지에서 에디터픽과 추천 코스의 최근 4개 코스 조회
  // 엔드포인트가 너무 건방짐. 메인페이지인지 어떻/게 알아. > main 이런식으로 해야지
  @GetMapping
  public ResponseEntity<?> mainPageList(){
    AdminUserTopListDto mainList = placeMainPageService.mainPagelist();
    return ResponseEntity.ok(mainList);
  }

  // 에디터픽 코스 목록 조회 (일반 사용자용) : 메서드명 수정 필요 > editorCourseList
  @GetMapping("/editor-recommend-courses")
  public ResponseEntity<?> editorCourseList(){
    List<EditorCourseDto> adminList = placeMainPageService.adminPageList();
    return ResponseEntity.ok(adminList);
  }

  // 추천 (사용자 픽) 코스 목록 조회
  @GetMapping("/recommend-courses")
  public ResponseEntity<ApiResponse<Page<CourseDto>>> recommendCourseList(
      @RequestParam(required = false) List<String> hashtags,
      @RequestParam(defaultValue = "1") int page // 쿼리 파라미터로 변경
  ){
    Page<CourseDto> courseList = placeMainPageService.recommendCourseService(hashtags, page);
    return ResponseEntity.ok(ApiResponse.success(courseList));
  }

  // 추천 코스 상세정보 조회
  @GetMapping("/recommend-courses/{recommend_id}")
  public ResponseEntity<?> recommendCourseDetail(@PathVariable Long recommend_id){
    CourseDetailDto course = placeMainPageService.userDetailPlace(recommend_id);
    return ResponseEntity.ok(course);
  }

  // 에디터픽 코스 상세정보 조회
  // 메서드명 일관성 있게 수정 > editorCourseDetail
  @GetMapping("/editor-recommend-courses/{recommend_id}")
  public ResponseEntity<?> editorCourseDetail(@PathVariable Long recommend_id){
    EditorDetailCourseDto course = placeMainPageService.editorDetailPlace(recommend_id);
    return ResponseEntity.ok(course);
  }

  // 추천 코스 상세정보 페이지 전체 리뷰 조회
  @GetMapping("/recommend-courses/{recommendId}/reviews")
  public ResponseEntity<?> reviewAll(@PathVariable Long recommendId){
    List<ResponseReviewDto> reviewlist = reviewService.reviewlist(recommendId);
    return ResponseEntity.ok(reviewlist);
  }

  // 추천 코스 상세정보 페이지 리뷰 등록
  // 메서드명 > reviewRegist 같은 걸로 바꾸면 좋을듯
  // recommend id 를 DTO 에 넣으면 안되나?
  @PostMapping("/recommend-courses/{recommend_id}")
  public ResponseEntity<?> reviewUpload(@PathVariable Long recommend_id,@RequestBody RequestReviewDto dto,@AuthenticationPrincipal
      Principal principal){

    String userNum = principal.getUsername();
    reviewService.reviewUpload(recommend_id,dto,userNum);
    return ResponseEntity.ok(ApiResponse.success("리뷰 등록이 되었습니다"));
  }

  // 추천 코스 상세정보 페이지 등록 리뷰 삭제
  @PostMapping("/recommend-courses/delete/{review_id}")
  public ResponseEntity<?> reviewDelete(@PathVariable Long review_id){
    reviewService.reviewDelete(review_id);
    return ResponseEntity.ok(ApiResponse.success("리뷰 삭제가 되었습니다"));
  }

  // 추천 코스 좋아요 등록
  // 좋아요 이거를 favorites or likes 하나로 통일하자
  // 메서드명도 addLikeCourse 이런걸로 기능을 직관적으로 알 수 있게 수정하자
  @PostMapping("/users/{recommend_id}/likes")
  public ResponseEntity<?> likeCourse(@PathVariable Long recommend_id,@AuthenticationPrincipal Principal principal){
    String userId = principal.getUsername();
    favoriteService.userFavoriteAdd(recommend_id, userId);
    return ResponseEntity.ok(ApiResponse.success("유저 코스 찜하기"));
  }

  // 에디터픽 코스 좋아요 등록
  // 메서드명 > addLikeEditorCourse 이런걸로 바꿔
  @PostMapping("/admin/{recommend_id}/likes")
  public ResponseEntity<?> likeAdminCourse(@PathVariable Long recommend_id,@AuthenticationPrincipal Principal principal){
    String userId = principal.getUsername();
    favoriteService.adminFavoriteAdd(recommend_id, userId);
    return ResponseEntity.ok(ApiResponse.success("관리자 코스 찜하기"));
  }

  // 좋아요 수 가장 많은 1개 불러오기. activated 처리 완료
  @GetMapping("/top-liked-course")
  public ResponseEntity<CourseDto> getTopLikedCourse() {
    CourseDto top = courseService.getTopLikedCourse();
    return ResponseEntity.ok(top);
  }



}
