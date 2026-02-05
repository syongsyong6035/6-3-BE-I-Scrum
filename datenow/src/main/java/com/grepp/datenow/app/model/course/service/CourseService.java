package com.grepp.datenow.app.model.course.service;

import com.grepp.datenow.app.model.course.dto.CourseDetailDto;
import com.grepp.datenow.app.model.course.dto.CourseDto;
import com.grepp.datenow.app.model.course.dto.MyCourseResponse;
import com.grepp.datenow.app.model.course.dto.MyDateCourseDto;
import com.grepp.datenow.app.model.course.entity.Course;
import com.grepp.datenow.app.model.course.entity.Hashtag;
import com.grepp.datenow.app.model.course.entity.RecommendCourse;
import com.grepp.datenow.app.model.course.repository.HashtagRepository;
import com.grepp.datenow.app.model.course.repository.custom.RecommendCourseRepository;
import com.grepp.datenow.app.model.course.repository.RegistMyCourseRepository;
import com.grepp.datenow.app.model.image.entity.Image;
import com.grepp.datenow.app.model.image.repository.ImageRepository;
import com.grepp.datenow.app.model.member.entity.Member;
import com.grepp.datenow.app.model.course.entity.CourseHashtag;
import com.grepp.datenow.app.model.place.dto.PlaceDetailDto;
import com.grepp.datenow.infra.error.exception.course.BadWordsException;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import com.grepp.datenow.app.model.course.repository.MyCourseRepository;
import com.grepp.datenow.app.model.place.entity.Place;
import com.grepp.datenow.app.model.place.repository.PlaceRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;
import java.io.File;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class CourseService {

    private final MyCourseRepository myCourseRepository;
    private final PlaceRepository placeRepository;
    private final RegistMyCourseRepository courseRepository;
    private final RecommendCourseRepository recommendCourseRepository;
    private final ImageRepository imageRepository;
    private final HashtagRepository hashtagRepository;
    private final ModelMapper modelMapper;
    private final BadWordFilterService badWordFilterService;

    @Value("${upload.path}")
    private String uploadPath;

    @PostConstruct
    public void init() {
        // src/main/resources/static/image 디렉토리 사용
        String projectRoot = System.getProperty("user.dir");
        this.uploadPath = projectRoot + File.separator + "src" + File.separator + "main" + 
                         File.separator + "resources" + File.separator + "static" + 
                         File.separator + "images";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        log.info("File upload path initialized to: {}", uploadPath);
    }

    @Transactional
    public void saveCourse(MyDateCourseDto dto, Member member) {
        // 1. 기본 Course 저장 (내용, 작성자 등)
        Course course = Course.builder()
            .title(dto.title())
            .description(dto.description())
            .build();
        myCourseRepository.save(course);

        // 2. RecommendCourse 생성 (Course와 1:1 관계)
        RecommendCourse recommendCourse = RecommendCourse.builder()
            .course(course)
            .id(member) // 작성자 정보는 RecommendCourse.id 에 저장
            .build();
        recommendCourseRepository.save(recommendCourse);

        // 3. 해시태그 처리 (RecommendCourse에 연결!)
        if (dto.hashtagNames() != null) {
            for (String name : dto.hashtagNames()) {
                String trimmedName = name.trim();
                if (trimmedName.isEmpty()) continue;

                // 비속어 필터링
                if (badWordFilterService.containBadWords(trimmedName)) {
                    throw new BadWordsException("부적절한 해시태그: " + trimmedName);
                }

                // 해시태그 조회 또는 생성
                Hashtag hashtag = hashtagRepository.findByTagName(trimmedName)
                    .orElseGet(() -> hashtagRepository.save(new Hashtag(trimmedName)));

                // [핵심] CourseHashtag를 RecommendCourse에 연결 (연관관계 편의 메서드 사용)
                recommendCourse.addCourseHashtag(hashtag);
            }
        }
    }

    public Course getCourseById(Long courseId) {
        log.info("Finding course with ID: {}", courseId);
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> {
                log.error("Course not found with id: {}", courseId);
                return new EntityNotFoundException("Course not found with id: " + courseId);
            });
        log.info("Found course: {}", course);
        return course;
    }

    @Transactional
    public void registerToRecommendCourse(Long courseId, List<String> imageUrls) {
        Course course = getCourseById(courseId);

        if (imageUrls == null || imageUrls.isEmpty()) {
            throw new IllegalArgumentException("추천 코스 등록을 위해서는 최소 1장의 이미지가 필요합니다.");
        }

        if (recommendCourseRepository.existsByCourse(course)) {
            throw new IllegalStateException("이미 추천 코스로 등록된 코스입니다.");
        }

        // 추천 코스 생성 및 저장
        RecommendCourse recommendCourse = RecommendCourse.builder()
            .course(course)
            .build();
        recommendCourseRepository.save(recommendCourse);

        // 이미지 처리
        for (String imagePath : imageUrls) {
            Image image = Image.builder()
                .recommendCourse(recommendCourse)
                .originFileName(extractFileName(imagePath))
                .renameFileName(extractFileName(imagePath))
                .savePath(imagePath)
                .type("image")
                .build();
            imageRepository.save(image);
        }
    }

    @Transactional(readOnly = true)
    public List<MyCourseResponse> findMyCourses(Member member) {
        List<Course> courses = myCourseRepository.findAllByMemberOrderByCreatedAtDesc(member);
        return courses.stream()
            .map(course -> new MyCourseResponse(course.getCoursesId(), course.getTitle()))
            .collect(Collectors.toList());
    }

    private String extractFileName(String path) {
        return path.substring(path.lastIndexOf('/') + 1);
    }
    @Transactional(readOnly = true)
    public CourseDetailDto getCourseDetail(Long courseId) {
        Course course = getCourseById(courseId);

        CourseDetailDto dto = modelMapper.map(course, CourseDetailDto.class);

        List<Place> places = placeRepository.findByCourseId(course);
        List<PlaceDetailDto> placeDtos = places.stream()
                .map(place -> {
                    return new PlaceDetailDto(place.getPlaceName(), place.getAddress());
                })
                .toList();
        dto.setPlaces(placeDtos);

        List<Image> images = imageRepository.findByRecommendCourse_Course(course);

        List<String> imageUrls = images.stream()
                .map(Image::getSavePath)
                .toList();
        dto.setImageUrl(imageUrls);
        RecommendCourse recommendCourse = recommendCourseRepository.findByCourse(course).orElseThrow();

        // Course 엔티티에서 CourseHashtag 컬렉션 가져오기
        List<String> hashtagNames = recommendCourse.getCourseHashtags().stream()
            // CourseHashtag 객체에서 실제 Hashtag 엔티티를 가져옴
            .map(CourseHashtag::getHashtag)
            // Hashtag 엔티티에서 tagName 을 가져옴
            .map(Hashtag::getTagName)
            .collect(Collectors.toList());
        dto.setHashtagNames(hashtagNames); // CourseDetailDto 에 해시태그 목록 설정

        return dto;
    }

    public CourseDto getTopLikedCourse() {
        RecommendCourse rc = recommendCourseRepository.findTopLikedRecommendCourse();
        return new CourseDto(rc);
    }
}