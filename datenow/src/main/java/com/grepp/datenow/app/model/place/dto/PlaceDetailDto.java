package com.grepp.datenow.app.model.place.dto;

import com.grepp.datenow.app.model.place.entity.Place;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PlaceDetailDto {
  String title;
  String address;

  public PlaceDetailDto() {
  }

  public PlaceDetailDto(Place place) {
    this.address = place.getAddress();
    this.title = place.getPlaceName();
  }

  public PlaceDetailDto(String title, String address) {
    this.title = title;
    this.address = address;
  }
}
