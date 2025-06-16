package com.grepp.datenow.infra.auth;

import com.grepp.datenow.app.model.auth.domain.Principal;
import com.grepp.datenow.app.model.member.entity.Member;
import com.grepp.datenow.app.model.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUserId(username)
                            .orElseThrow(() -> new UsernameNotFoundException(username));

        if (!member.getActivated() || member.isLeaved()) {
            throw new DisabledException("탈퇴된 회원입니다.");
        }

        List<SimpleGrantedAuthority> authorities = findAuthorities(username);
        return Principal.createPrincipal(member, authorities);
    }
    
    @Cacheable("user-authorities")
    public List<SimpleGrantedAuthority> findAuthorities(String username){
        Member member = memberRepository.findByUserId(username)
                            .orElseThrow(() -> new UsernameNotFoundException(username));
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(member.getRole().name()));

        return authorities;
    }

}
