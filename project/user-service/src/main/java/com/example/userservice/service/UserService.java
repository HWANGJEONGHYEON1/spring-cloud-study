package com.example.userservice.service;

import com.example.userservice.client.OrderServiceClient;
import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import com.example.userservice.utils.ModelMapperUtils;
import com.example.userservice.vo.ResponseOrder;
import com.example.userservice.vo.ResponseUser;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final OrderServiceClient orderServiceClient;

    private final CircuitBreakerFactory circuitBreakerFactory;

    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        UserEntity userEntity = ModelMapperUtils.modelMapper().map(userDto, UserEntity.class);
        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));

        userRepository.save(userEntity);

        return ModelMapperUtils.modelMapper()
                .map(userEntity, UserDto.class);
    }

    public ResponseUser getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }

        UserDto userDto = ModelMapperUtils.modelMapper()
                .map(userEntity, UserDto.class);

        /* FeignException handling*/
//        List<ResponseOrder> orderList = orderServiceClient.getOrders(userId);
        CircuitBreaker circuitbreaker = circuitBreakerFactory.create("circuitbreaker");

        /*
            order-service 중단된 상태에서도 서비스 장애로 이루어지지 않고 빈 리스트를 리턴해줌
         */
        log.info("Before order-service");
        List<ResponseOrder> orderList = circuitbreaker.run(() -> orderServiceClient.getOrders(userId),
                throwable -> new ArrayList<>());
        log.info("after order-service");
        userDto.setOrders(orderList);

        return ModelMapperUtils.modelMapper()
                .map(userDto, ResponseUser.class);
    }

    public List<ResponseUser> getUserByAll() {
        List<ResponseUser> result = Lists.newArrayList();

        userRepository.findAll()
                .forEach(v -> {
                    result.add(ModelMapperUtils.modelMapper()
                            .map(v, ResponseUser.class));
                });

        return result;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException("user not found");
        }

        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
                true, true, true, true,
                new ArrayList<>());
    }

    public UserDto getUserDetailsByEmail(String username) {
        UserEntity userEntity = userRepository.findByEmail(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        return userDto;
    }
}
