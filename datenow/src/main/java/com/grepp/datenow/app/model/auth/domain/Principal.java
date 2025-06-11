package com.grepp.datenow.app.model.auth.domain;

import com.grepp.datenow.app.model.member.entity.Member;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class Principal extends User {

    private final String email;

    public Principal(String username, String password, String email,
        Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.email = email;
    }

    public static Principal createPrincipal(Member member,
        List<SimpleGrantedAuthority> authorities){
        return new Principal(member.getUserId(), member.getPassword(), member.getEmail(), authorities);
    }

    public String getEmail() {
        return email;
    }
}
