package com.grepp.datenow.app.model.place.service;

import com.grepp.datenow.app.model.course.dto.CourseDetailDto;
import com.grepp.datenow.app.model.course.dto.CourseDto;
import com.grepp.datenow.app.model.course.dto.EditorCourseDto;
import com.grepp.datenow.app.model.course.dto.EditorDetailCourseDto;
import com.grepp.datenow.app.model.course.entity.Course;
import com.grepp.datenow.app.model.course.entity.EditorCourse;
import com.grepp.datenow.app.model.course.entity.RecommendCourse;
import com.grepp.datenow.app.model.course.repository.AdminCourseRepository;
import com.grepp.datenow.app.model.course.repository.CourseRepository;
import com.grepp.datenow.app.model.image.entity.Image;
import com.grepp.datenow.app.model.image.repository.ImageRepository;
import com.grepp.datenow.app.model.like.repository.FavoriteRepository;
import com.grepp.datenow.app.model.place.dto.PlaceDetailDto;
import com.grepp.datenow.app.model.place.dto.mainpage.AdminUserTopListDto;
import com.grepp.datenow.app.model.place.entity.Place;
import com.grepp.datenow.app.model.place.repository.PlaceRepository;
import com.grepp.datenow.app.model.review.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
          Image img = imageRepository.findFirstByRecommendCourseId(course)
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
  public List<CourseDto> recommendCourseService() {
    List<RecommendCourse> userPlace = courseRepository.findAllWithCourseAndMember();
    List<CourseDto> courseDto = userPlace.stream()
        .map(course -> {
          Image img = imageRepository.findFirstByRecommendCourseId(course)
              .orElse(null);
          int count = favoriteRepository.countByRecommendCourseAndActivatedTrue(course);
          int reviewCnt = reviewRepository.countByRecommendCourseIdAndActivatedTrue(course);
          String imageUrl = (img != null)
              ? "/images/" +  img.getRenameFileName()
              : "/images/bg_night.jpg";
          return new CourseDto(course,imageUrl,count,reviewCnt);

        })
        .toList();
    return courseDto;

  }


  @Transactional
  public CourseDetailDto userDetailPlace(Long recommendId) {

    RecommendCourse recommendCourse = courseRepository.findWithCourseAndMemberById(recommendId)
        .orElseThrow(() -> new EntityNotFoundException("엔티티가 존재하지 않습니다."));
    CourseDetailDto placeDetail = new CourseDetailDto();
    placeDetail.setTitle(recommendCourse.getCourseId().getTitle());
    placeDetail.setNickname(recommendCourse.getCourseId().getId().getNickname());
    placeDetail.setCreatedAt(recommendCourse.getCreatedAt());
    placeDetail.setDescription(recommendCourse.getCourseId().getDescription());
    Course course = recommendCourse.getCourseId();
    List<Place> places = placeRepository.findAllByCourseId(course);
    List<PlaceDetailDto> placeDetailDtos = places.stream()
            .map(PlaceDetailDto::new)
                .toList();

    placeDetail.setPlaces(placeDetailDtos);
    List<Image> image = imageRepository.findAllByRecommendCourseId(recommendCourse);
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
