package com.grepp.datenow.infra.config;

import com.grepp.datenow.app.controller.web.member.payload.SignupRequest;
import com.grepp.datenow.app.model.course.dto.CourseDetailDto;
import com.grepp.datenow.app.model.course.entity.Course;
import com.grepp.datenow.app.model.member.dto.MemberDto;
import com.grepp.datenow.app.model.member.entity.Member;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.getConfiguration().setPreferNestedProperties(false);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        modelMapper.typeMap(SignupRequest.class, Member.class).addMappings(mapper -> {mapper.skip(Member::setId);});
        modelMapper.typeMap(MemberDto.class, Member.class).addMappings(mapper -> mapper.skip(Member::setId));
        // Course -> CourseDetailDto 매핑 (작성자 정보는 RecommendCourse에서 별도로 세팅)
        modelMapper.typeMap(Course.class, CourseDetailDto.class)
            .addMapping(Course::getCoursesId, CourseDetailDto::setCoursesId);

        return modelMapper;
    }
    
}

