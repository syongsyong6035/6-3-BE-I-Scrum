package com.grepp.datenow.app.model.member.entity;

import com.grepp.datenow.app.model.auth.code.Role;
import com.grepp.datenow.infra.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String birth;

    @Column(nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable=false, columnDefinition="BIT DEFAULT 0")
    private boolean leaved = false;

    @Column(columnDefinition="DATETIME(6) DEFAULT NULL")
    private LocalDateTime leavedAt;

    @Column(nullable = false)
    private Boolean verified = false;

    private String verifyToken;

    private LocalDateTime tokenExpiredAt;

}