package com.ssafy.moida.service;

import com.ssafy.moida.api.request.UserJoinReqDto;
import com.ssafy.moida.model.Role;
import com.ssafy.moida.model.Users;
import com.ssafy.moida.repository.UserRepository;
import com.ssafy.moida.util.error.ErrorCode;
import com.ssafy.moida.util.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User에 관련된 각종 함수를 처리하는 서비스
 */

@Slf4j
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // 회원가입
    public void JoinUser(UserJoinReqDto userJoinReqDto) {
        //user role 확인
        if(userJoinReqDto.getRole().equals("ROLE_ADMIN")) {
            userJoinReqDto.setRole(Role.valueOf("ROLE_ADMIN"));
        } else {
            userJoinReqDto.setRole(Role.valueOf("ROLE_ADMIN"));
        }

        Users u = Users.builder().email(userJoinReqDto.getEmail())
                .password(bCryptPasswordEncoder.encode(userJoinReqDto.getPassword()))
                .phone(userJoinReqDto.getPhone())
                .nickname(userJoinReqDto.getNickname())
                .ticketCnt(0)
                .point(0L)
                .nftUrl(userJoinReqDto.getNftUrl())
                .walletUrl(userJoinReqDto.getWalletUrl())
                .role(userJoinReqDto.getRole())
                .build();

        userRepository.save(u);
    }

    // 닉네임 중복 검사
    public void DuplicatedUserByNickname(String nickname) {
        // 닉네임이 존재하면 true, 아니라면 false
        boolean userNickname = userRepository.existsByNickname(nickname);

        // 닉네임이 존재한다면 중복이므로 에러 던지기
        if(userNickname) {
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE);
        }

    }

    //비밀번호 정규 표현식 검사
    public void VaildUserByPassword(String password) {
        // 영문대소문자, 숫자, 특수문자 조합 8자이상 16자 이내
        Pattern pwdPattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,16}$");
        Matcher pwdMatcher = pwdPattern.matcher(password);

        // 비밀번호 정규 표현식을 검사 했을 때 조건에 맞다면 그냥 두기
        // 조건에 맞지 않다면 에러 던지기
        if(!pwdMatcher.find()) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }


    }

}
