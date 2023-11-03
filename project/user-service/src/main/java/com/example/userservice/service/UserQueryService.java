package com.example.userservice.service;

import com.example.userservice.client.OrderServiceClient;
import com.example.userservice.dto.UserCommand;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserQueryService {

    private final UserRepository userRepository;
    private final CircuitBreakerFactory circuitBreakerFactory;
    private final OrderServiceClient orderServiceClient;
    public ResponseUser getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }

        UserCommand userDto = ModelMapperUtils.modelMapper()
                .map(userEntity, UserCommand.class);

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

    public UserCommand getUserDetailsByEmail(String username) {
        UserEntity userEntity = userRepository.findByEmail(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }

        UserCommand userDto = new ModelMapper().map(userEntity, UserCommand.class);
        return userDto;
    }
}
