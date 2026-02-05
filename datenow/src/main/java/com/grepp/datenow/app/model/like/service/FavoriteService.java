package com.grepp.datenow.app.model.like.service;

import com.grepp.datenow.app.model.course.entity.EditorCourse;
import com.grepp.datenow.app.model.course.entity.RecommendCourse;
import com.grepp.datenow.app.model.course.repository.AdminCourseRepository;
import com.grepp.datenow.app.model.course.repository.CourseRepository;
import com.grepp.datenow.app.model.like.dto.FavoriteCourseResponse;
import com.grepp.datenow.app.model.like.entity.FavoriteCourse;
import com.grepp.datenow.app.model.like.repository.FavoriteRepository;
import com.grepp.datenow.app.model.member.entity.Member;
import com.grepp.datenow.app.model.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteService {

  private final FavoriteRepository favoriteRepository;
  private final MemberRepository memberRepository;
  private final CourseRepository courseRepository;
  private final AdminCourseRepository adminCourseRepository;

  @Transactional
  public void userFavoriteAdd(Long recommendId, String userId) {

    Member member = memberRepository.findByUserId(userId)
        .orElseThrow(()-> new EntityNotFoundException("로그인을 해주세요"));

    RecommendCourse recommendCourse = courseRepository.findById(recommendId)
        .orElseThrow(()-> new EntityNotFoundException("해당 코스가 존재하지 않습니다"));


    Optional<FavoriteCourse> favorite = favoriteRepository.findByMemberAndRecommendCourse(member, recommendCourse);

    if(favorite.isPresent()){
      FavoriteCourse favoriteCourse = favorite.get();
      favoriteCourse.setActivated(!favoriteCourse.getActivated());
      favoriteCourse.setModifiedAt(LocalDateTime.now());
    }
    else{
      FavoriteCourse favoriteCourse = FavoriteCourse.builder()
          .member(member)
          .recommendCourse(recommendCourse)
          .build();

      favoriteRepository.save(favoriteCourse);
    }



  }

  @Transactional
  public void adminFavoriteAdd(Long editorCourseId, String userId) {

    Member member = memberRepository.findByUserId(userId)
        .orElseThrow(()-> new EntityNotFoundException("로그인을 해주세요"));

    EditorCourse editorCourse = adminCourseRepository.findById(editorCourseId)
        .orElseThrow(()-> new EntityNotFoundException("해당 관리자 코스가 없습니다"));

    Optional<FavoriteCourse> favoriteeditor = favoriteRepository.findByMemberAndEditorCourse(member, editorCourse);

    if(favoriteeditor.isPresent()){
      FavoriteCourse favoriteCourse = favoriteeditor.get();
      favoriteCourse.setActivated(!favoriteCourse.getActivated());
      favoriteCourse.setModifiedAt(LocalDateTime.now());
    }
    else{
      FavoriteCourse favoriteCourse = FavoriteCourse.builder()
          .editorCourse(editorCourse)
          .member(member)
          .build();

      favoriteRepository.save(favoriteCourse);

    }



  }

  public boolean isLiked(String userId, Long recommendId) {
    Member member = memberRepository.findByUserId(userId)
        .orElseThrow(()-> new EntityNotFoundException("로그인을 해주세요"));

    RecommendCourse recommendCourse = courseRepository.findById(recommendId)
        .orElseThrow(()-> new EntityNotFoundException("해당 코스가 존재하지 않습니다"));

    return favoriteRepository.existsByMemberAndRecommendCourseAndActivatedTrue(member,recommendCourse);
  }

  public boolean isEditorLiked(String userId, Long recommendId) {
    Member member = memberRepository.findByUserId(userId)
        .orElseThrow(()-> new EntityNotFoundException("로그인을 해주세요"));

    EditorCourse editorCourse = adminCourseRepository.findById(recommendId)
        .orElseThrow(()-> new EntityNotFoundException("해당 관리자 코스가 없습니다"));

    return favoriteRepository.existsByMemberAndEditorCourseAndActivatedTrue(member,editorCourse);
  }

  @Transactional(readOnly = true)
  public List<FavoriteCourseResponse> getFavoriteCourses(String userId) {
    Member member = memberRepository.findByUserId(userId)
        .orElseThrow(() -> new EntityNotFoundException("로그인을 해주세요"));

    List<FavoriteCourse> favorites = favoriteRepository.findByMemberId(member.getId());

    return favorites.stream().map(fav -> {
      Long recommendCourseId = null;
      Long editorCourseId = null;
      String title = null;
      String nickname = null;

      if (fav.getRecommendCourse() != null && fav.getRecommendCourse().getCourse() != null) {
        recommendCourseId = fav.getRecommendCourse().getRecommendCourseId();
        title = fav.getRecommendCourse().getCourse().getTitle();
        nickname = fav.getRecommendCourse().getId().getNickname();
      } else if (fav.getEditorCourse() != null) {
        editorCourseId = fav.getEditorCourse().getEditorCourseId();
        title = fav.getEditorCourse().getTitle();
        nickname = fav.getEditorCourse().getMember().getNickname();
      }

      return new FavoriteCourseResponse(
          fav.getFavoriteCourseId(),
          nickname,
          recommendCourseId,
          editorCourseId,
          title
      );
    }).toList();
  }

  @Transactional
  public void deactivateFavorite(Long favoriteCourseId) {
    FavoriteCourse favorite = favoriteRepository.findById(favoriteCourseId)
        .orElseThrow(() -> new EntityNotFoundException("찜 정보를 찾을 수 없습니다."));
    favorite.setActivated(false);
    favorite.setModifiedAt(LocalDateTime.now());
  }
}