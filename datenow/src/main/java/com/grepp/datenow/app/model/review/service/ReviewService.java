package com.grepp.datenow.app.model.review.service;

import com.grepp.datenow.app.model.course.entity.RecommendCourse;
import com.grepp.datenow.app.model.course.repository.CourseRepository;
import com.grepp.datenow.app.model.member.entity.Member;
import com.grepp.datenow.app.model.member.repository.MemberRepository;
import com.grepp.datenow.app.model.review.dto.RequestReviewDto;
import com.grepp.datenow.app.model.review.dto.ResponseReviewDto;
import com.grepp.datenow.app.model.review.entity.Review;
import com.grepp.datenow.app.model.review.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewRepository reviewRepository;
  private final CourseRepository courseRepository;
  private final MemberRepository memberRepository;

  @Transactional
  public void reviewUpload(Long recommendId, RequestReviewDto dto, String userNum) {

    RecommendCourse recommendCourse = courseRepository.findById(recommendId)
        .orElseThrow(()->new EntityNotFoundException("해당 코스를 찾을수 없습니다"));

    Member member = memberRepository.findByUserId(userNum)
        .orElseThrow(()->new EntityNotFoundException("해당 유저를 찾을수 없습니다"));

    Review review = new Review(recommendCourse,member,dto.getContent(),dto.getStar());

    reviewRepository.save(review);

  }

  @Transactional
  public List<ResponseReviewDto> reviewlist(Long recommendId) {

      RecommendCourse recommendCourse = courseRepository.findById(recommendId)
          .orElseThrow(()->new EntityNotFoundException("해당 코스를 찾을수 없습니다"));

      List<Review> reviews = reviewRepository.findAllByRecommendCourseIdAndActivatedTrue(recommendCourse);
    List<ResponseReviewDto> reviewDtoList = reviews.stream()
        .map(ResponseReviewDto::new)
        .toList();

    return reviewDtoList;

  }

  @Transactional
  public void reviewDelete( Long reviewId) {
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(()-> new EntityNotFoundException("해당 리뷰가 존재하지 않습니다"));
    review.setActivated(false);
    reviewRepository.save(review);
  }
}
