package com.ephemzy.springsecurityclient.controller;


import com.ephemzy.springsecurityclient.entity.User;
import com.ephemzy.springsecurityclient.event.RegistrationCompleteEvent;
import com.ephemzy.springsecurityclient.model.UserModel;
import com.ephemzy.springsecurityclient.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;
    @PostMapping("/register")
    public String registerUser(
            @RequestBody UserModel userModel,
            final HttpServletRequest request
    ){
        User user = userService.registerUser(userModel);
        publisher.publishEvent(
                new RegistrationCompleteEvent(
                        user,
                        applicationUrl(request)
                )
        );
        return "Success";
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://"
                + request.getServerName()
                + ":"
                + request.getServerPort()
                + request.getContextPath();
    }

}
