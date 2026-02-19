package kr.or.kids.domain.ca.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kids.domain.ca.auth.service.AuthService;
import kr.or.kids.domain.ca.auth.vo.LogoutPVO;
import kr.or.kids.domain.ca.auth.vo.MbrTokenDVO;
import kr.or.kids.domain.ca.auth.vo.MbrTokenPVO;
import kr.or.kids.domain.ca.auth.vo.RefreshPVO;
import kr.or.kids.global.system.common.vo.ApiPrnDto;

@Tag(name = "Auth1Controller", description = "대국민포털_로그인토큰 관리(JWT)")
@RestController
@RequestMapping(value="/api/ca/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 로그인 처리
     *
     * 1. Refresh / Access Token 생성(JWT)
     * 2. JWT 토큰 값을 DB에 저장(신규면 insert, 기존에 있으면 update)
     * 3. 회원정보 중 비밀번호오류횟수를 0으로 초기화함(회원정보 DB 업데이트)
     * 4. Redis Idle 키 생성(30분)
     * 5. Redis Active 키 생성(Access Token 만료기준으로 설정)
     *
     * @param input
     * @return
     */
    @Operation(summary = "로그인 처리(Redis Idle 키 생성, Redis Active 키 생성 포함)", description = "로그인 처리(Redis Idle 키 생성, Redis Active 키 생성 포함)한다.(JWT)")
    @PostMapping(value="/login")
    @ResponseBody
    public ResponseEntity<ApiPrnDto> login(@RequestBody MbrTokenPVO input){
        ApiPrnDto apiPrnDto = authService.login(input);

        if("0".equals(apiPrnDto.getCode())){
            return ResponseEntity.ok(apiPrnDto);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiPrnDto);
        }
    }

    /**
     * JWT 토큰 갱신
     *
     * 1. JWT Refresh 토큰 문자열에서 회원ID(mbrId) 가져옴.
     * 2. 기존 JWT 토큰 정보 업데이트(DB)
     * 3. Refresh도 사용자 활동이므로 Redis Idle Key 30분 리셋
     * 4. Redis Active Key 만료시간도 리셋
     *
     * old parameter - @RequestHeader("X-Refresh-Token") String refreshToken, @RequestHeader(value = "X-App-Id", required = false) String appId
     * @param refreshToken
     * @param appId
     * @return
     */
    @Operation(summary = "JWT 토큰 갱신(Redis Idle 키 리셋, Redis Active 키 리셋 포함)", description = "JWT 토큰 갱신(Redis Idle 키 리셋, Redis Active 키 리셋 포함)한다.")
    @PostMapping("/refresh")
    @ResponseBody
    public ResponseEntity<ApiPrnDto> refresh(@RequestBody RefreshPVO refreshPVO){
        ApiPrnDto apiPrnDto = authService.refresh(refreshPVO.getTokenId(), refreshPVO.getRefreshToken());

        if("0".equals(apiPrnDto.getCode())){
            return ResponseEntity.ok(apiPrnDto);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiPrnDto);
        }
    }

    /**
     * 로그아웃 처리
     * 1. DB에서 refresh/access 정보 삭제(token_id + mbr_id 조건)
     * 2. Redis Idle 키, Redis Active 키 삭제
     * 3. 삭제된 Access Token을 Redis 블랙리스트에 등록 (로그아웃 즉시 무효화)
     * 
     * @param auth
     * @param appId
     * @param tokenId
     * @param authorization
     * @return
     */
    @Operation(summary = "로그아웃 처리(Redis Idle 키 갱신, Redis Active 키 갱신 포함)", description = "JWT 토큰 갱신(Redis Idle 키 갱신, Redis Active 키 갱신 포함)한다.")
    @PostMapping("/logout")
    @ResponseBody
    public ResponseEntity<ApiPrnDto> logout(Authentication auth, @RequestBody LogoutPVO loginPVO, @RequestHeader(value = "Authorization", required = false) String authorization){
        String mbrId = (String) auth.getPrincipal();

        MbrTokenDVO mtd = new MbrTokenDVO();
        mtd.setTokenSn(loginPVO.getTokenSn());
        mtd.setMbrId(mbrId);

        ApiPrnDto apiPrnDto = authService.logout(mtd, authorization);

        if("0".equals(apiPrnDto.getCode())){
            return ResponseEntity.ok(apiPrnDto);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiPrnDto);
        }
    }

    /**
     * Redis Idle 키 리셋
     * @param authorization
     * @return
     */
    @PostMapping("/extend")
    @ResponseBody
    public ResponseEntity<ApiPrnDto> extend(
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        ApiPrnDto apiPrnDto = authService.extend(authorization);

        if("0".equals(apiPrnDto.getCode())){
            return ResponseEntity.ok(apiPrnDto);
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiPrnDto);
        }
    }
}
