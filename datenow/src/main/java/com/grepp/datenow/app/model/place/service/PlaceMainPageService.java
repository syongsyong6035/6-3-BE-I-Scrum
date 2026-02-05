package com.grepp.datenow.app.model.place.service;

import com.grepp.datenow.app.model.course.dto.CourseDetailDto;
import com.grepp.datenow.app.model.course.dto.CourseDto;

import com.grepp.datenow.app.model.course.dto.EditorCourseDto;
import com.grepp.datenow.app.model.course.dto.EditorDetailCourseDto;
import com.grepp.datenow.app.model.course.entity.Course;
import com.grepp.datenow.app.model.course.entity.EditorCourse;
import com.grepp.datenow.app.model.course.entity.RecommendCourse;
import com.grepp.datenow.app.model.course.entity.CourseHashtag;
import com.grepp.datenow.app.model.course.entity.Hashtag;
import com.grepp.datenow.app.model.course.repository.AdminCourseRepository;
import com.grepp.datenow.app.model.course.repository.CourseRepository;
import com.grepp.datenow.app.model.course.repository.custom.RecommendCourseRepository;
import com.grepp.datenow.app.model.image.entity.Image;
import com.grepp.datenow.app.model.image.repository.ImageRepository;
import com.grepp.datenow.app.model.like.repository.FavoriteRepository;
import com.grepp.datenow.app.model.place.dto.PlaceDetailDto;
import com.grepp.datenow.app.model.place.dto.mainpage.AdminUserTopListDto;
import com.grepp.datenow.app.model.place.entity.Place;
import com.grepp.datenow.app.model.place.repository.PlaceRepository;
import com.grepp.datenow.app.model.review.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaceMainPageService {

  private final AdminCourseRepository adminCourseRepository;
  private final CourseRepository courseRepository;
  private final ImageRepository imageRepository;
  private final PlaceRepository placeRepository;
  private final ReviewRepository reviewRepository;
  private final FavoriteRepository favoriteRepository;
  private final RecommendCourseRepository recommendCourseRepository;


  @Transactional
  public AdminUserTopListDto mainPagelist() {


      Pageable pageable =  PageRequest.of(0, 4);
      List<EditorCourse> adminplace = adminCourseRepository.findTop4ByOrderByCreatedAtDesc(pageable);
      List<RecommendCourse> userplace = courseRepository.findTop4ByOrderByCreatedAtDesc(pageable);


    List<EditorCourseDto> adminDto = adminplace.stream()
        .map(course -> {
          Image img = imageRepository.findFirstByEditorCourseId(course).orElse(null);
          int likeCount = favoriteRepository.countByEditorCourseAndActivatedTrue(course);


          String imageUrl = (img != null)
              ? "/images/" + img.getRenameFileName()
              : "/images/bg_night.jpg";

          return new EditorCourseDto(course, imageUrl, likeCount);
        })
        .toList();



    List<CourseDto> userDto = userplace.stream()
        .map(course -> {
          Image img = imageRepository.findFirstByRecommendCourse(course)
              .orElse(null);
          int count = favoriteRepository.countByRecommendCourseAndActivatedTrue(course);
          int reviewCnt = reviewRepository.countByRecommendCourseIdAndActivatedTrue(course);
          String imageUrl = (img != null)
              ? "/images/" +  img.getRenameFileName()
              : "/images/bg_night.jpg";
          return new CourseDto(course, imageUrl, count,reviewCnt);

        })
        .toList();

    return new AdminUserTopListDto(adminDto,userDto);




  }
  @Transactional
  public List<EditorCourseDto> adminPageList() {
    List<EditorCourse> adminPlace = adminCourseRepository.findAllByActivatedTrue();
    List<EditorCourseDto> adminDto = adminPlace.stream()
        .map(course -> {
          Image img = imageRepository.findFirstByEditorCourseId(course).orElse(null);
          int likeCount = favoriteRepository.countByEditorCourseAndActivatedTrue(course);
          String imageUrl = (img != null)
              ? "/images/" + img.getRenameFileName()
              : "/images/bg_night.jpg";
          return new EditorCourseDto(course, imageUrl, likeCount);
        })
        .toList();

    return adminDto;

  }
  @Transactional
  public Page<CourseDto> recommendCourseService(List<String> hashtagNames, int page) {
      Page<RecommendCourse> userPlace;

      if (hashtagNames != null && !hashtagNames.isEmpty()) {
        //가져올때
          userPlace = recommendCourseRepository.findAllCourseWithHashtags(hashtagNames, page, 10);
      } else {

          userPlace = recommendCourseRepository.findAllWithCourseAndMember(page, 10);
      }
      Page<CourseDto> courseDtoPage = userPlace.map(CourseDto::new);
      return courseDtoPage;

  }


  @Transactional
  public CourseDetailDto userDetailPlace(Long recommendId) {

    RecommendCourse recommendCourse = courseRepository.findWithCourseAndMemberById(recommendId)
        .orElseThrow(() -> new EntityNotFoundException("엔티티가 존재하지 않습니다."));
    CourseDetailDto placeDetail = new CourseDetailDto();
    placeDetail.setTitle(recommendCourse.getCourse().getTitle());
    placeDetail.setNickname(recommendCourse.getId().getName());
    placeDetail.setCreatedAt(recommendCourse.getCreatedAt());
    placeDetail.setDescription(recommendCourse.getCourse().getDescription());
    Course course = recommendCourse.getCourse();
    List<Place> places = placeRepository.findAllByCourseId(course);
    List<PlaceDetailDto> placeDetailDtos = places.stream()
            .map(PlaceDetailDto::new)
                .toList();

    placeDetail.setPlaces(placeDetailDtos);
    List<Image> image = imageRepository.findAllByRecommendCourse(recommendCourse);
    List<String> imageUrl = image.stream()
        .map(img -> {
          if(img != null){
            return "/images/" + img.getRenameFileName();
          }
          else {
            return  "/images/bg_night.jpg";
          }
        })
            .toList();

    placeDetail.setImageUrl(imageUrl);
      // ⭐⭐⭐ 해시태그 정보 매핑 (새로 추가) ⭐⭐⭐
      // RecommendCourse 엔티티에서 CourseHashtag 컬렉션 가져오기
      List<String> hashtagNames = recommendCourse.getCourseHashtags().stream()
          // CourseHashtag 객체에서 실제 Hashtag 엔티티를 가져옴
          .map(CourseHashtag::getHashtag)
          // Hashtag 엔티티에서 tagName을 가져옴
          .map(Hashtag::getTagName)
          // Stream의 결과를 List<String>으로 수집
          .collect(Collectors.toList());
      placeDetail.setHashtagNames(hashtagNames); // CourseDetailDto에 해시태그 목록 설정
      // ⭐⭐⭐ 끝 ⭐⭐⭐

    return placeDetail;



  }

  @Transactional
  public EditorDetailCourseDto editorDetailPlace(Long editorId) {
    EditorCourse editorCourse = adminCourseRepository.findWithMemberById(editorId)
        .orElseThrow(() -> new EntityNotFoundException("엔티티가 존재하지 않습니다."));
    EditorDetailCourseDto placeDetail = new EditorDetailCourseDto();
    placeDetail.setTitle(editorCourse.getTitle());
    placeDetail.setNickname(editorCourse.getMember().getNickname());
    placeDetail.setCreatedAt(editorCourse.getCreatedAt());
    placeDetail.setDescription(editorCourse.getDescription());

    List<Place> places = placeRepository.findAllByEditorCourseId(editorCourse);
    List<PlaceDetailDto> placeDetailDtos = places.stream()
        .map(PlaceDetailDto::new)
        .toList();

    placeDetail.setPlaces(placeDetailDtos);
    List<Image> image = imageRepository.findAllByEditorCourseId(editorCourse);
    List<String> imageUrl = image.stream()
        .map(img -> {
          if(img != null){
            return "/images/" + img.getRenameFileName();
          }
          else {
            return  "/images/bg_night.jpg";
          }
        })
        .toList();

    placeDetail.setImageUrl(imageUrl);
    return placeDetail;

  }
}
