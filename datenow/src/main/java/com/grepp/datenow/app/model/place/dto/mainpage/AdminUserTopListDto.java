package com.grepp.datenow.app.model.place.dto.mainpage;

import com.grepp.datenow.app.model.course.dto.CourseDto;
import com.grepp.datenow.app.model.course.dto.EditorCourseDto;
import java.util.List;
import lombok.Getter;

@Getter
public class AdminUserTopListDto {

  private List<CourseDto> userlist;
  private List<EditorCourseDto> adminlist;

  public AdminUserTopListDto(List<EditorCourseDto> adminlist,
      List<CourseDto> userlist) {
    this.adminlist = adminlist;
    this.userlist = userlist;
  }


}
