package com.ephemzy.springsecurityclient.event.listener;

import com.ephemzy.springsecurityclient.entity.User;
import com.ephemzy.springsecurityclient.event.RegistrationCompleteEvent;
import com.ephemzy.springsecurityclient.service.UserService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {



    @Autowired
    private UserService userService;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // Create the Verification Token for the user with link
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token, user);
        //Send mail to user
        String url = event.getApplicationUrl()
                + "verifyRegistration?token="
                + token;

        log.info("Click the link to verify your account: {}");
    }
}
