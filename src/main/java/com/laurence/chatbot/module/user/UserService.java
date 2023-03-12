package com.laurence.chatbot.module.user;

import com.laurence.chatbot.common.utils.JwtTokenUtil;
import com.laurence.chatbot.common.utils.JwtUserDetailsService;
import com.laurence.chatbot.enums.ResponseEnum;
import com.laurence.chatbot.exception.BusinessException;
import com.laurence.chatbot.module.user.request.UserLoginRequest;
import com.laurence.chatbot.module.user.response.UserLoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    public UserLoginResponse validateUser(UserLoginRequest userLoginRequest){
        UserDetails user = jwtUserDetailsService.loadUserByUsername(userLoginRequest.getUsername());
        boolean validUser = passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword());
        if(!validUser){
            String message = String.format(ResponseEnum.FAILED_PASSWORD.getMessage(), userLoginRequest.getUsername());
            throw new BusinessException(ResponseEnum.FAILED_PASSWORD, message);
        }
        String token = jwtTokenUtil.generateToken(user);
        return UserLoginResponse.builder()
                .token(token)
                .username(user.getUsername())
                .build();
    }
}
