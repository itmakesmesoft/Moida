package com.ssafy.moida.service.user;

import com.ssafy.moida.api.response.GetUserVolunteerResDto;
import com.ssafy.moida.model.project.Status;
import com.ssafy.moida.model.user.UsersVolunteer;
import com.ssafy.moida.repository.user.UsersVolunteerRepository;
import com.ssafy.moida.utils.error.ErrorCode;
import com.ssafy.moida.utils.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class UserVolunteerService {

    private final UsersVolunteerRepository usersVolunteerRepository;

    public UserVolunteerService(UsersVolunteerRepository usersVolunteerRepository) {
        this.usersVolunteerRepository = usersVolunteerRepository;
    }

    /**
     * [한선영] 사용자가 참여한 봉사의 개수 가져오기
     * @return
     * */
    public long totalVolunteerCnt() {
        // 봉사 프로젝트 개수 가져오기
        return usersVolunteerRepository.countBy();
    }

    /**
     * [한선영] 사용자가 참여한 봉사 프로젝트 목록(GetUserVolunteerResDto) 가져오기
     * @param userId
     * @return
     * */
    public List<GetUserVolunteerResDto> getUsersVolunteer(Long userId) {
        List<GetUserVolunteerResDto> result = new ArrayList<>();
        result = usersVolunteerRepository.findVolunteersByUserId(userId);

        return result;
    }

    /**
     * [세은] UsersVolunteer 객체를 id로 찾기
     * @param id
     * @return
     */
    public UsersVolunteer findUsersVolunteerById(Long id){
        return usersVolunteerRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.DATA_NOT_FOUND));
    }

    /**
     * [세은] UsersVolunteer 객체가 존재하는지 확인
     * @param id
     * @return
     */
    public boolean existsById(Long id){
        return usersVolunteerRepository.existsById(id);
    }

    /**
     * [세은] 사용자 봉사 신청 취소
     * @param usersVolunteer
     * @param status
     */
    @Transactional
    public void updateUserVolunteerStatus(UsersVolunteer usersVolunteer, Status status){
        usersVolunteer.updateStatus(status);
    }

}