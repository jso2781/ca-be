package kr.or.kids.domain.ca.mbr.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import kr.or.kids.global.system.common.ApiResultCode;
import kr.or.kids.global.system.common.vo.PageRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kids.domain.ca.mbr.service.MbrInfoService;
import kr.or.kids.domain.ca.mbr.vo.MbrInfoDVO;
import kr.or.kids.domain.ca.mbr.vo.MbrInfoPVO;
import kr.or.kids.domain.ca.mbr.vo.MbrInfoRVO;
import kr.or.kids.global.system.common.vo.ApiPrnDto;

import javax.servlet.http.HttpServletRequest;

@Tag(name = "MbrInfoController", description = "대국민포털_회원정보기본 관리")
@RestController
@RequestMapping(value="/api/ca/mbr")
public class MbrInfoController
{
    @Autowired
    private MbrInfoService mbrInfoService;


    /**
     * 회원목록
     * @param  pageRequestDto 페이지용 데이터
     * @param request HTTP 요청
     * @return API 응답
     */
    @Operation( summary = "회원 목록 조회", description = "회원 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "회원 조회 성공")
    @PostMapping("/list")
    public ResponseEntity<ApiPrnDto> mebrList(@RequestBody PageRequestDto pageRequestDto, HttpServletRequest request) {

        int pageNum = pageRequestDto != null ? pageRequestDto.getPageNum() : 1;
        int pageSize = pageRequestDto != null ? pageRequestDto.getPageSize() : 10;

        ApiPrnDto apiPrnDto = mbrInfoService.list(pageNum, pageSize);
        ApiResultCode resultCode = ApiResultCode.fromCode(apiPrnDto.getCode());
        return ResponseEntity.status(resultCode.getHttpStatus()).body(apiPrnDto);
    }


    @Operation(summary = "대국민포털_회원정보기본 기존 아이디, 이메일 존재여부 조회", description = "대국민포털_회원정보기본 기존 아이디, 이메일 존재여부 조회한다.")
    @PostMapping(value="/checkMbrInfo")
    @ResponseBody
    public ResponseEntity<ApiPrnDto> checkMbrInfo(@RequestBody MbrInfoPVO mbrInfoPVO)
    {
        ApiPrnDto apiPrnDto = mbrInfoService.checkMbrInfo(mbrInfoPVO);

        HashMap<String, Object> dataMap = apiPrnDto.getData();

        Integer checkCnt = (Integer) dataMap.get("checkCnt");
        if(0 == checkCnt){
            return ResponseEntity.ok(apiPrnDto);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiPrnDto);
        }
    }

    @Operation(summary = "대국민포털_회원정보기본 조회", description = "대국민포털_회원정보기본 조회한다.")
    @PostMapping(value="/getMbrInfo")
    @ResponseBody
    public ResponseEntity<MbrInfoRVO> getMbrInfo(@RequestBody MbrInfoPVO mbrInfoPVO)
    {
        MbrInfoRVO mbrInfo = mbrInfoService.getMbrInfo(mbrInfoPVO);

        return ResponseEntity.ok(mbrInfo);
    }

    @Operation(summary = "대국민포털_회원정보기본 입력", description = "대국민포털_회원정보기본 입력한다.")
    @PostMapping(value="/insertMbrInfo")
    @ResponseBody
    public Map<String,Object> insertMbrInfo(@RequestBody List<MbrInfoPVO> mbrInfoList)
    {
        int mbrInfoListCount = mbrInfoList.size();

        int insertCnt = 0;
        MbrInfoPVO mbrInfo = null;

        for(int i=0;i<mbrInfoListCount;i++)
        {
            mbrInfo = mbrInfoList.get(i);

            mbrInfoService.insertMbrInfo(mbrInfo);
            insertCnt++;

            mbrInfo = null;
        }

        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("insertCnt", insertCnt);

        return resultMap;
    }

    @Operation(summary = "대국민포털_회원정보기본 수정", description = "대국민포털_회원정보기본 수정한다.")
    @PostMapping(value="/updateMbrInfo")
    @ResponseBody
    public Map<String,Object> updateMbrInfo(@RequestBody List<MbrInfoPVO> mbrInfoList)
    {
        int mbrInfoListCount = mbrInfoList.size();

        int updateCnt = 0;
        MbrInfoPVO mbrInfo = null;

        for(int i=0;i<mbrInfoListCount;i++)
        {
            mbrInfo = mbrInfoList.get(i);

            mbrInfoService.updateMbrInfo(mbrInfo);
            updateCnt++;

            mbrInfo = null;
        }

        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("updateCnt", updateCnt);

        return resultMap;
    }

    @Operation(summary = "대국민포털_회원정보기본 저장", description = "대국민포털_회원정보기본 저장한다.")
    @PostMapping(value="/saveMbrInfo")
    @ResponseBody
    public Map<String,Object> saveMbrInfo(@RequestBody List<MbrInfoPVO> mbrInfoList)
    {
        int mbrInfoListCount = mbrInfoList.size();

        int saveCnt = 0;
        MbrInfoPVO mbrInfo = null;

        for(int i=0;i<mbrInfoListCount;i++)
        {
            mbrInfo = mbrInfoList.get(i);

            mbrInfoService.saveMbrInfo(mbrInfo);
            saveCnt++;

            mbrInfo = null;
        }

        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("saveCnt", saveCnt);

        return resultMap;
    }

    @Operation(summary = "대국민포털_회원정보기본 삭제", description = "대국민포털_회원정보기본 삭제한다.")
    @PostMapping(value="/deleteMbrInfo")
    @ResponseBody
    public Map<String,Object> deleteMbrInfo(@RequestBody MbrInfoDVO mbrInfoDVO)
    {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        int deleteCnt = mbrInfoService.deleteMbrInfo(mbrInfoDVO);

        resultMap.put("deleteCnt", deleteCnt);

        return resultMap;
    }
}
