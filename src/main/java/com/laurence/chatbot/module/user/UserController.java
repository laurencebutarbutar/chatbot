package com.laurence.chatbot.module.user;

import com.laurence.chatbot.common.logging.LogExecutionTime;
import com.laurence.chatbot.module.base.constant.ApiPath;
import com.laurence.chatbot.module.user.request.UserLoginRequest;
import com.laurence.chatbot.module.user.response.UserLoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = ApiPath.USER,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;

    @LogExecutionTime
    @PostMapping(value = ApiPath.LOGIN, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserLoginResponse login(@RequestBody @Valid UserLoginRequest userLoginRequest) {
        log.info("#login() with user : {}", userLoginRequest);
        return userService.validateUser(userLoginRequest);
    }
}
