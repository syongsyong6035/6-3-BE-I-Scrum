package com.grepp.datenow.app.model.course.service;

import com.grepp.datenow.app.model.course.dto.EditorCourseSaveDto;
import com.grepp.datenow.app.model.course.entity.EditorCourse;
import com.grepp.datenow.app.model.course.repository.EditorCourseRepository;
import com.grepp.datenow.app.model.image.entity.Image;
import com.grepp.datenow.app.model.image.repository.ImageRepository;
import com.grepp.datenow.app.model.member.entity.Member;
import com.grepp.datenow.app.model.place.dto.PlaceSaveDto;
import com.grepp.datenow.app.model.place.entity.Place;
import com.grepp.datenow.app.model.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EditorCourseService {

    private final EditorCourseRepository editorCourseRepository;
    private final PlaceRepository placeRepository;
    private final ImageRepository imageRepository;

    public void save(EditorCourseSaveDto dto, Member member) {
        // 1. 에디터 코스 저장
        EditorCourse course = new EditorCourse();
        course.setTitle(dto.title());
        course.setDescription(dto.description());
        course.setActivated(true);
        course.setMember(member);  // member 필드에 직접 설정
        editorCourseRepository.save(course);

        // 2. 장소 저장
        for (PlaceSaveDto placeDto : dto.places()) {
            Place place = Place.builder()
                .editorCourseId(course)
                .placeName(placeDto.placeName())
                .address(placeDto.address())
                .latitude(placeDto.latitude())
                .longitude(placeDto.longitude())
                .build();
            placeRepository.save(place);
        }

        // 3. 이미지 정보 저장 (이미 업로드된 경로를 이용함)
        for (String imagePath : dto.imageUrls()) {
            Image image = Image.builder()
                .editorCourseId(course)
                .originFileName(extractFileName(imagePath))
                .renameFileName(extractFileName(imagePath))
                .savePath(imagePath)
                .type("image")
                .build();
            imageRepository.save(image);
        }
    }

    private String extractFileName(String path) {
        return path.substring(path.lastIndexOf('/') + 1);
    }
}