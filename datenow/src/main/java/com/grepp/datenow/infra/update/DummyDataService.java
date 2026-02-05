package com.grepp.datenow.infra.update;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DummyDataService {

  private final JdbcTemplate jdbcTemplate;

  public void bulkInsertForPerformanceTest() {

    // 1. Member 1,000명 생성
    //insertMembers(1000);

    // 2. Course 50,000건 생성 (Member ID 참조)
    //insertCourses(50000);

    // 3. EditorCourse / RecommendCourse 생성 (Course, Member 참조)
   // insertEditorCourses(5000);
    insertRecommendCourses(45000);

    // 4. Hashtag 및 중간 테이블 생성
    insertHashtags(100);
    insertCourseHashtags(150000); // RecommendCourse ID, Tag ID 참조

    // 5. 기타 데이터
    insertImages(5000, 45000);
    insertFavorites(100000);
    insertReviews(100000);
  }

  private void insertMembers(int count) {
    String sql = "INSERT INTO member (user_id, password, email, name, nickname, role, birth, phone, activated, leaved) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        // 기존 유저가 있으므로 충분히 큰 숫자(예: 1000)를 더해 중복 방지
        int uniqueNum = i + 1000;
        ps.setString(1, "user_" + uniqueNum);
        ps.setString(2, "pass123!");
        ps.setString(3, "test" + uniqueNum + "@test.com");
        ps.setString(4, "이름" + uniqueNum);
        ps.setString(5, "닉네임" + uniqueNum);
        ps.setString(6, "ROLE_USER");
        ps.setString(7, "1995-01-01");
        ps.setString(8, "010-0000-" + String.format("%04d", i));
        ps.setBoolean(9, true);
        ps.setInt(10, 0);
      }
      @Override
      public int getBatchSize() { return count; }
    });
  }

  private void insertCourseHashtags(int count) {
    String sql = "INSERT INTO course_hashtag (recommend_course_id, tag_id) VALUES (?, ?)";

    // 실제 존재하는 RecommendCourse / Hashtag ID만 사용
    List<Long> recommendIds = jdbcTemplate.queryForList(
        "SELECT recommend_course_id FROM recommend_course", Long.class);
    List<Long> hashtagIds = jdbcTemplate.queryForList(
        "SELECT tag_id FROM hashtag", Long.class);

    if (recommendIds.isEmpty() || hashtagIds.isEmpty()) return;

    jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        long recId = recommendIds.get(
            ThreadLocalRandom.current().nextInt(recommendIds.size()));
        long tagId = hashtagIds.get(
            ThreadLocalRandom.current().nextInt(hashtagIds.size()));

        ps.setLong(1, recId);
        ps.setLong(2, tagId);
      }
      @Override
      public int getBatchSize() { return count; }
    });
  }

  private void insertCourses(int count) {
    String sql = "INSERT INTO course (title, description, created_at, modified_at) VALUES (?, ?, NOW(), NOW())";


    jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setString(1, "코스 " + i);
        ps.setString(2, "설명 " + i);
      }
      @Override
      public int getBatchSize() { return count; }
    });
  }


  private void insertEditorCourses(int count) {
    String sql = "INSERT INTO editor_course (title, description, id, activated, created_at, modified_at) VALUES (?, ?, ?, true, NOW(), NOW())";

    // 실제 존재하는 Member ID 중 하나를 사용
    List<Long> memberIds = jdbcTemplate.queryForList(
        "SELECT id FROM member", Long.class);
    if (memberIds.isEmpty()) return;
    jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setString(1, "에디터 코스 " + i);
        ps.setString(2, "상세설명");
        long memberId = memberIds.get(
            ThreadLocalRandom.current().nextInt(memberIds.size()));
        ps.setLong(3, memberId);
      }
      @Override
      public int getBatchSize() { return count; }
    });
  }


  private void insertImages(int editorCount, int recommendCount) {
    // 1. 필수 필드(origin, rename, save_path)를 모두 포함한 SQL
    String sql = "INSERT INTO image (origin_file_name, rename_file_name, save_path, editor_course_id, recommend_course_id, created_at, modified_at) " +
        "VALUES (?, ?, ?, ?, ?, NOW(), NOW())";

    // --- EditorCourse용 이미지 삽입 ---
    // 실제 존재하는 EditorCourse ID 목록
    List<Long> editorIds = jdbcTemplate.queryForList(
        "SELECT editor_course_id FROM editor_course", Long.class);

    if (!editorIds.isEmpty()) {
      jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {
          ps.setString(1, "editor_origin_" + i + ".jpg");
          ps.setString(2, "editor_rename_" + i + ".jpg");
          ps.setString(3, "/static/images/editor/");
          long editorId = editorIds.get(
              ThreadLocalRandom.current().nextInt(editorIds.size()));
          ps.setLong(4, editorId);
          ps.setNull(5, java.sql.Types.BIGINT); // recommend_course_id는 null
        }
        @Override
        public int getBatchSize() { return editorCount; }
      });
    }

    // --- RecommendCourse용 이미지 삽입 ---
    List<Long> recIds = jdbcTemplate.queryForList(
        "SELECT recommend_course_id FROM recommend_course", Long.class);

    if (!recIds.isEmpty()) {
      jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {
          ps.setString(1, "rec_origin_" + i + ".jpg");
          ps.setString(2, "rec_rename_" + i + ".jpg");
          ps.setString(3, "/static/images/recommend/");
          ps.setNull(4, java.sql.Types.BIGINT); // editor_course_id는 null
          long recId = recIds.get(
              ThreadLocalRandom.current().nextInt(recIds.size()));
          ps.setLong(5, recId);
        }
        @Override
        public int getBatchSize() { return recommendCount; }
      });
    }
  }

  private void insertFavorites(int count) {
    // 에러 로그에 CONSTRAINT FOREIGN KEY (id) REFERENCES member (id) 라고 되어있으므로 'id' 컬럼 사용
    String sql = "INSERT INTO favorite_course (id, editor_course_id, recommend_course_id, activated) VALUES (?, ?, ?, true)";

    List<Long> editorIds = jdbcTemplate.queryForList(
        "SELECT editor_course_id FROM editor_course", Long.class);
    List<Long> recIds = jdbcTemplate.queryForList(
        "SELECT recommend_course_id FROM recommend_course", Long.class);

    jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setLong(1, 1L); // 즐겨찾기 누른 사람을 1번으로 통일

        if (Math.random() > 0.5 && !editorIds.isEmpty()) {
          long editorId = editorIds.get(
              ThreadLocalRandom.current().nextInt(editorIds.size()));
          ps.setLong(2, editorId);
          ps.setNull(3, java.sql.Types.BIGINT);
        } else if (!recIds.isEmpty()) {
          ps.setNull(2, java.sql.Types.BIGINT);
          long recId = recIds.get(
              ThreadLocalRandom.current().nextInt(recIds.size()));
          ps.setLong(3, recId);
        }
      }
      @Override
      public int getBatchSize() { return count; }
    });
  }
  private void insertReviews(int count) {
    // 1. SQL 문에 star 컬럼 추가
    String sql = "INSERT INTO review (content, recommend_course_id, id, star, activated, created_at) VALUES (?, ?, ?, ?, true, NOW())";

    List<Long> recIds = jdbcTemplate.queryForList(
        "SELECT recommend_course_id FROM recommend_course", Long.class);

    if (recIds.isEmpty()) return;

    jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setString(1, "최고의 코스네요! " + i);

        long recId = recIds.get(
            ThreadLocalRandom.current().nextInt(recIds.size()));
        ps.setLong(2, recId);

        // 작성자 1번 고정
        ps.setLong(3, 1L);

        // 2. 별점(star) 추가: 1~5점 사이 랜덤
        int randomStar = (int) (Math.random() * 5) + 1;
        ps.setInt(4, randomStar);
      }
      @Override
      public int getBatchSize() { return count; }
    });
  }
  private void insertRecommendCourses(int count) {
    // 1. 현재 DB에 있는 Course ID의 최소값을 가져옵니다 (270,000 예상)
    Long minCourseId = jdbcTemplate.queryForObject("SELECT MIN(courses_id) FROM course", Long.class);

    if (minCourseId == null) return;
    // 실제 존재하는 Member ID 중 하나를 사용
    List<Long> memberIds = jdbcTemplate.queryForList(
        "SELECT id FROM member", Long.class);
    if (memberIds.isEmpty()) return;

    String sql = "INSERT INTO recommend_course (courses_id, member_id, created_at, modified_at) VALUES (?, ?, NOW(), NOW() )";

    jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        // 2. EditorCourse가 1~2000번을 쓴다면, Recommend는 그 이후부터 매핑
        // minCourseId(270,000) + 2000 + i
        long targetCourseId = minCourseId + 2000 + i;

        ps.setLong(1, targetCourseId);
        long memberId = memberIds.get(
            ThreadLocalRandom.current().nextInt(memberIds.size()));
        ps.setLong(2, memberId);
      }
      @Override
      public int getBatchSize() { return count; }
    });
  }

  private void insertHashtags(int count) {
    String sql = "INSERT INTO hashtag (tag_name) VALUES (?)";
    jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setString(1, "태그" + i);
      }
      @Override
      public int getBatchSize() { return count; }
    });
  }


}