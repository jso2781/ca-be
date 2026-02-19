package kr.or.kids.domain.ca.auth.mapper;


import kr.or.kids.domain.ca.auth.vo.UserLoginReqVO;
import kr.or.kids.domain.ca.auth.vo.UserLoginResVO;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {

    /**  로그인 사용자 정보 조회 */
    UserLoginResVO loginData(UserLoginReqVO param);

}
