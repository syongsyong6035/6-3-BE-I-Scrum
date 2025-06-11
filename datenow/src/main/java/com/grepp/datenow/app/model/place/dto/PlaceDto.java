package com.grepp.datenow.app.model.place.dto;

public record PlaceDto(
    String placeName,
    String address,
    String reason
) {}