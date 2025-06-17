package com.grepp.datenow.app.model.course.service;

import com.grepp.datenow.app.model.course.dto.CourseDetailDto;
import com.grepp.datenow.app.model.course.dto.CourseDto;
import com.grepp.datenow.app.model.course.dto.MyCourseResponse;
import com.grepp.datenow.app.model.course.dto.MyDateCourseDto;
import com.grepp.datenow.app.model.course.entity.Course;
import com.grepp.datenow.app.model.course.entity.Hashtag;
import com.grepp.datenow.app.model.course.entity.RecommendCourse;
import com.grepp.datenow.app.model.course.repository.HashtagRepository;
import com.grepp.datenow.app.model.course.repository.RecommendCourseRepository;
import com.grepp.datenow.app.model.course.repository.RegistMyCourseRepository;
import com.grepp.datenow.app.model.image.entity.Image;
import com.grepp.datenow.app.model.image.repository.ImageRepository;
import com.grepp.datenow.app.model.member.entity.Member;
import com.grepp.datenow.app.model.course.entity.CourseHashtag;
import com.grepp.datenow.app.model.place.dto.PlaceDetailDto;
import com.grepp.datenow.infra.error.exception.course.BadWordsException;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import com.grepp.datenow.app.model.course.repository.MyCourseRepository;
import com.grepp.datenow.app.model.place.dto.PlaceSaveDto;
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

        // 비속어 필터링
        if (badWordFilterService.containBadWords(dto.title())) {
            throw new BadWordsException("제목에 부적절한 단어가 포함되어 있습니다.");
        }

        if (badWordFilterService.containBadWords(dto.description())) {
            throw new BadWordsException("소개글에 부적절한 단어가 포함되어 있습니다.");
        }

        // Course 저장
        Course course = Course.builder()
            .id(member)
            .title(dto.title())
            .description(dto.description())
            .build();
        myCourseRepository.save(course);

        // Place 저장
        for (PlaceSaveDto placeDto : dto.places()) {
            Place place = Place.builder()
                .courseId(course)
                .placeName(placeDto.placeName())
                .address(placeDto.address())
                .placeUrl("")
                .latitude(0)
                .longitude(0)
                .build();
            placeRepository.save(place);
        }

        // 해시태그 저장 및 연결
        if (dto.hashtagNames() != null && !dto.hashtagNames().isEmpty()) {
            for (String hashtagName : dto.hashtagNames()) {
                String trimmedHashtagName = hashtagName.trim(); // 공백 제거

                // 빈 문자열은 패스
                if (trimmedHashtagName.isEmpty()) {
                    continue;
                }
                // 해시태그 비속어 필터링
                if (badWordFilterService.containBadWords(trimmedHashtagName)) {
                    throw new BadWordsException("해시태그에 부적절한 단어가 포함되어 있습니다:" + trimmedHashtagName);
                }

                // 해시태그가 이미 존재하면 그거를 쓰고 아니면 새로 만들어서 저장
                Hashtag hashtag = hashtagRepository.findByTagName(trimmedHashtagName)
                    .orElseGet(() -> {
                        Hashtag newhashtag = new Hashtag();
                        newhashtag.setTagName(trimmedHashtagName);
                        return hashtagRepository.save(newhashtag);
                    });
                // Course 와 Hashtag 의 연관관계를 만들어주기
                // 이거 없으면 CourseHashtag 엔티티에 아무것도 안생김 -> 연관관계 알 수 없음
                course.addCourseHashtag(hashtag);
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

        if (recommendCourseRepository.existsByCourseId(course)) {
            throw new IllegalStateException("이미 추천 코스로 등록된 코스입니다.");
        }

        // 추천 코스 생성 및 저장
        RecommendCourse recommendCourse = RecommendCourse.builder()
            .courseId(course)
            .build();
        recommendCourseRepository.save(recommendCourse);

        // 이미지 처리
        for (String imagePath : imageUrls) {
            Image image = Image.builder()
                .recommendCourseId(recommendCourse)
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
        List<Course> courses = myCourseRepository.findById(member);
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

        List<Image> images = imageRepository.findByRecommendCourseId_CourseId(course);

        List<String> imageUrls = images.stream()
                .map(Image::getSavePath)
                .toList();
        dto.setImageUrl(imageUrls);

        // Course 엔티티에서 CourseHashtag 컬렉션 가져오기
        List<String> hashtagNames = course.getCourseHashtags().stream()
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