package com.laurence.chatbot.common.utils;

import com.laurence.chatbot.enums.ResponseEnum;
import com.laurence.chatbot.exception.BusinessException;
import com.laurence.chatbot.repository.mongo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.laurence.chatbot.entity.mongo.User user = userRepository.findFirstByUsername(username);
        if(ObjectUtils.isNotEmpty(user)){
            return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
        } else {
            String message = String.format(ResponseEnum.USER_NOT_FOUND.getMessage(), username);
            throw new BusinessException(ResponseEnum.USER_NOT_FOUND, message);
        }
    }
}
