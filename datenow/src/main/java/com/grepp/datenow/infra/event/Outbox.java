package com.grepp.datenow.infra.event;

import com.grepp.datenow.infra.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor // for JPA.
@AllArgsConstructor // for @Builder.
public class Outbox extends BaseEntity {

    @Id
    @Builder.Default
    private String eventId = UUID.randomUUID().toString();
    private String eventType;
    private String sourceService = "datenow";
    private String payload;
}
