package com.grepp.datenow.app.model.place.entity;

import com.grepp.datenow.app.model.recommend.code.Category;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class HotPlace {

    @Id
    private Long placeId;
    private String placeName;
    @Enumerated(EnumType.STRING)
    private Category category;
    private String address;
    private Double latitude;
    private Double longitude;

    @Override
    public String toString() {
        return "HotPlace{" +
            "placeId=" + placeId +
            ", placeName='" + placeName + '\'' +
            ", category=" + category +
            ", address='" + address + '\'' +
            ", latitude=" + latitude +
            ", longitude=" + longitude +
            '}';
    }
}
