package com.grepp.datenow.app.model.place.dto;

public record PlaceSaveDto(
    String placeName,
    String address,
    double latitude,
    double longitude
) {

}
