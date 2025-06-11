package com.grepp.datenow.infra.init;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HotPlaceJson {
    private Long place_id;
    private String place_name;
    private String category;
    private String address;
    private Double latitude;
    private Double longitude;
    private List<String> keywords;

}
