package com.grepp.datenow.app.model.place.document;

import com.grepp.datenow.app.model.recommend.code.Category;
import jakarta.persistence.Id;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "places")
public class HotPlaceEmbedding {

    @Id
    private String id;
    private String text; // 전체 정보 텍스트 임베딩 (기존)
    private float[] embedding; // 전체 정보 텍스트 임베딩 벡터 (기존)

    private String placeName;
    private Category category;
    private String address;
    private String latitude;
    private String longitude;
    private List<String> keywords;

    public HotPlaceEmbedding(String id, String placeName, String category, String address, String latitude, String longitude, List<String> keywords, String text, float[] embedding) {
        this.id = id;
        this.placeName = placeName;
        this.category = Category.valueOf(category.toUpperCase());
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.keywords = keywords;
        this.text = text;
        this.embedding = embedding;
    }

    @Override
    public String toString() {
        return "HotPlaceEmbedding{" +
            "id='" + id + '\'' +
            ", placeName='" + placeName + '\'' +
            ", category=" + category +
            ", address='" + address + '\'' +
            ", latitude='" + latitude + '\'' +
            ", longitude='" + longitude + '\'' +
            ", keywords=" + keywords +
            '}';
    }
}
