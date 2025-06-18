package com.grepp.datenow.infra.auth.oauth2.user;

import java.util.Map;

public class GithubOAuth2UserInfo implements OAuth2UserInfo{
    
    private final Map<String, Object> attributes;
    
    public GithubOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    
    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }
    
    @Override
    public String getProvider() {
        return "github";
    }
    
    @Override
    public String getName() {
        return attributes.get("login").toString();
    }


}
