package com.grepp.datenow.app.model.member.service;

import com.grepp.datenow.app.model.course.dto.EditorCourseDto;
import com.grepp.datenow.app.model.course.entity.EditorCourse;
import com.grepp.datenow.app.model.course.repository.AdminCourseRepository;
import com.grepp.datenow.app.model.image.entity.Image;
import com.grepp.datenow.app.model.image.repository.ImageRepository;
import com.grepp.datenow.app.model.like.repository.FavoriteRepository;
import com.grepp.datenow.app.model.member.dto.AdminSearchUserDto;
import com.grepp.datenow.app.model.member.entity.Member;
import com.grepp.datenow.app.model.member.repository.AdminRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

public class AdminService {

    private final AdminRepository adminRepository;
    private final AdminCourseRepository adminCourseRepository;
    private final ImageRepository imageRepository;
    private final FavoriteRepository favoriteRepository;

    @Transactional
    public List<AdminSearchUserDto> userAllSearch() {

        List<Member> userAll = adminRepository.findAll();
        List<AdminSearchUserDto> userDtos = userAll.stream()
            .map(AdminSearchUserDto::new)
            .toList();

        return userDtos;
    }

    // 에디터 픽 코스 관리 및 목록
    @Transactional
    public List<EditorCourseDto> adminAllCourse() {
        List<EditorCourse> adminPlace = adminCourseRepository.findAllByActivatedTrue();

        return adminPlace.stream()
            .map(course -> {
                Image img = imageRepository.findFirstByEditorCourseId(course).orElse(null);
                int count = favoriteRepository.countByEditorCourse(course);

                String imageUrl = (img != null)
                    ? "/images/" + img.getRenameFileName() // ✅ 설정된 웹 경로 + 파일명
                    : "/images/bg_night.jpg";         // 기본 이미지도 동일하게

                return new EditorCourseDto(course, imageUrl,count);
            })
            .toList();
    }

    @Transactional
    public void adminRecommendDelete(Long recommendId) {

        EditorCourse editorCourse = adminCourseRepository.findById(recommendId)
            .orElseThrow(()->new EntityNotFoundException("해당 코스를 찾을수 없습니다"));
        editorCourse.setActivated(false);

    }
}
