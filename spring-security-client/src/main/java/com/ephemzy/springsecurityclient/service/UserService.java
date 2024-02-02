package com.ephemzy.springsecurityclient.service;

import com.ephemzy.springsecurityclient.entity.User;
import com.ephemzy.springsecurityclient.model.UserModel;

public interface UserService {
    User registerUser(UserModel userModel);

    void saveVerificationTokenForUser(String token, User user);
}
