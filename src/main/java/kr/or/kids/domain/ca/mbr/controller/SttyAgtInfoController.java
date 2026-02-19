package kr.or.kids.domain.ca.mbr.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kids.domain.ca.mbr.service.SttyAgtInfoService;
import kr.or.kids.domain.ca.mbr.vo.SttyAgtInfoDVO;
import kr.or.kids.domain.ca.mbr.vo.SttyAgtInfoPVO;
import kr.or.kids.domain.ca.mbr.vo.SttyAgtInfoRVO;

@Tag(name = "SttyAgtInfoController", description = "대국민포털_법정대리인정보기본 관리")
@RestController
@RequestMapping(value="/api/mbr")
public class SttyAgtInfoController
{
    @Autowired
    private SttyAgtInfoService sttyAgtInfoService;

    @Operation(summary = "대국민포털_법정대리인정보기본 조회", description = "대국민포털_법정대리인정보기본 조회한다.")
    @PostMapping(value="/getSttyAgtInfo")
    @ResponseBody
    public ResponseEntity<SttyAgtInfoRVO> getSttyAgtInfo(@RequestBody SttyAgtInfoPVO sttyAgtInfoPVO)
    {
        SttyAgtInfoRVO sttyAgtInfo = sttyAgtInfoService.getSttyAgtInfo(sttyAgtInfoPVO);

        return ResponseEntity.ok(sttyAgtInfo);
    }

    @Operation(summary = "대국민포털_법정대리인정보기본 입력", description = "대국민포털_법정대리인정보기본 입력한다.")
    @PostMapping(value="/insertSttyAgtInfo")
    @ResponseBody
    public Map<String,Object> insertSttyAgtInfo(@RequestBody List<SttyAgtInfoPVO> sttyAgtInfoList)
    {
        int sttyAgtInfoListCount = sttyAgtInfoList.size();

        int insertCnt = 0;
        SttyAgtInfoPVO sttyAgtInfo = null;

        for(int i=0;i<sttyAgtInfoListCount;i++)
        {
            sttyAgtInfo = sttyAgtInfoList.get(i);

            sttyAgtInfoService.insertSttyAgtInfo(sttyAgtInfo);
            insertCnt++;

            sttyAgtInfo = null;
        }

        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("insertCnt", insertCnt);

        return resultMap;
    }

    @Operation(summary = "대국민포털_법정대리인정보기본 수정", description = "대국민포털_법정대리인정보기본 수정한다.")
    @PostMapping(value="/updateSttyAgtInfo")
    @ResponseBody
    public Map<String,Object> updateSttyAgtInfo(@RequestBody List<SttyAgtInfoPVO> sttyAgtInfoList)
    {
        int sttyAgtInfoListCount = sttyAgtInfoList.size();

        int updateCnt = 0;
        SttyAgtInfoPVO sttyAgtInfo = null;

        for(int i=0;i<sttyAgtInfoListCount;i++)
        {
            sttyAgtInfo = sttyAgtInfoList.get(i);

            sttyAgtInfoService.updateSttyAgtInfo(sttyAgtInfo);
            updateCnt++;

            sttyAgtInfo = null;
        }

        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("updateCnt", updateCnt);

        return resultMap;
    }

    @Operation(summary = "대국민포털_법정대리인정보기본 저장", description = "대국민포털_법정대리인정보기본 저장한다.")
    @PostMapping(value="/saveSttyAgtInfo")
    @ResponseBody
    public Map<String,Object> saveSttyAgtInfo(@RequestBody List<SttyAgtInfoPVO> sttyAgtInfoList)
    {
        int sttyAgtInfoListCount = sttyAgtInfoList.size();

        int saveCnt = 0;
        SttyAgtInfoPVO sttyAgtInfo = null;

        for(int i=0;i<sttyAgtInfoListCount;i++)
        {
            sttyAgtInfo = sttyAgtInfoList.get(i);

            sttyAgtInfoService.saveSttyAgtInfo(sttyAgtInfo);
            saveCnt++;

            sttyAgtInfo = null;
        }

        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("saveCnt", saveCnt);

        return resultMap;
    }

    @Operation(summary = "대국민포털_법정대리인정보기본 삭제", description = "대국민포털_법정대리인정보기본 삭제한다.")
    @PostMapping(value="/deleteSttyAgtInfo")
    @ResponseBody
    public Map<String,Object> deleteSttyAgtInfo(@RequestBody SttyAgtInfoDVO sttyAgtInfoDVO)
    {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        int deleteCnt = sttyAgtInfoService.deleteSttyAgtInfo(sttyAgtInfoDVO);

        resultMap.put("deleteCnt", deleteCnt);

        return resultMap;
    }
}
