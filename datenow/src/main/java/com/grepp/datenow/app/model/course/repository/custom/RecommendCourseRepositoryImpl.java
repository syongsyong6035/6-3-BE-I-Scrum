package com.grepp.datenow.app.model.course.repository.custom;

import com.grepp.datenow.app.model.course.entity.QCourse;
import com.grepp.datenow.app.model.course.entity.QCourseHashtag;
import com.grepp.datenow.app.model.course.entity.QHashtag;
import com.grepp.datenow.app.model.course.entity.QRecommendCourse;
import com.grepp.datenow.app.model.course.entity.RecommendCourse;

import com.grepp.datenow.app.model.member.entity.QMember;
import com.grepp.datenow.app.model.review.entity.QReview;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@RequiredArgsConstructor
public class RecommendCourseRepositoryImpl implements RecommendCourseRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  /**
   * 해시태그 목록으로 추천 코스를 조회.
   * RecommendCourse 기준으로 이미지, 해시태그는 배치 지정해서 가져오는 방식
   * course,member은 패치 조인으로 데이터 정합성 깨질일 없음 ~~
   */
  @Override
  public Page<RecommendCourse> findAllCourseWithHashtags(List<String> hashtagNames, int page, int size) {
    QRecommendCourse rc = QRecommendCourse.recommendCourse;
    QMember m = QMember.member;
    QHashtag h = QHashtag.hashtag;
    QCourse c = QCourse.course;
    QCourseHashtag ch = QCourseHashtag.courseHashtag;

    List<RecommendCourse> content = jpaQueryFactory.selectFrom(rc)
        .join(rc.id, m).fetchJoin()
        .join(rc.course, c).fetchJoin()
        .leftJoin(rc.courseHashtags, ch)
        .leftJoin(ch.hashtag, h)
        .where(hashtagIn(h, hashtagNames))
        .offset((long) (page - 1) * size)
        .limit(size)
        .fetch();
    // 데이터 갯수
    Long total = jpaQueryFactory.select(rc.count())
        .from(rc)
        .leftJoin(rc.courseHashtags, ch)
        .leftJoin(ch.hashtag, h)
        .where(hashtagIn(h, hashtagNames))
        .fetchOne();

    return new PageImpl<>(content, PageRequest.of(page - 1, size), total != null ? total : 0L);
  }

  private BooleanExpression hashtagIn(QHashtag h, List<String> hashtagNames) {
    return (hashtagNames == null || hashtagNames.isEmpty()) ? null : h.tagName.in(hashtagNames);
  }

  /**
   * RecommendCourse 기준으로 이미지, 해시태그는 배치 지정해서 가져오는 방식
   * course,member은 패치 조인으로 데이터 정합성 깨질일 없음 ~~
   */
  @Override
  public Page<RecommendCourse> findAllWithCourseAndMember(int page, int size) {
    QRecommendCourse rc = QRecommendCourse.recommendCourse;
    QMember m = QMember.member;
    QHashtag h = QHashtag.hashtag;
    QCourse c = QCourse.course;
    QCourseHashtag ch = QCourseHashtag.courseHashtag;

    List<RecommendCourse> content = jpaQueryFactory.selectFrom(rc)
        .join(rc.id, m).fetchJoin()
        .join(rc.course, c).fetchJoin()
        .leftJoin(rc.courseHashtags, ch)
        .leftJoin(ch.hashtag, h)
        .offset((long) (page - 1) * size)
        .limit(size)
        .fetch();

    Long total = jpaQueryFactory.select(rc.count())
        .from(rc)
        .leftJoin(rc.courseHashtags, ch)
        .leftJoin(ch.hashtag, h)
        .fetchOne();

    return new PageImpl<>(content, PageRequest.of(page - 1, size), total != null ? total : 0L);
  }
}
