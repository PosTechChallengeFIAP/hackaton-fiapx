package com.fiapx.videoprocessor.adapters.driven.infra.auth.usermanagement.mapper;

import com.fiapx.videoprocessor.adapters.driven.infra.auth.usermanagement.model.UserInfo;
import com.fiapx.videoprocessor.core.domain.entities.User;

import java.util.Objects;

public class UserInfo2User {
    public static User map(UserInfo userInfo){
        if(Objects.isNull(userInfo)) return null;

        User user = new User();
        user.setUsername(userInfo.getUsername());
        return user;
    }
}
