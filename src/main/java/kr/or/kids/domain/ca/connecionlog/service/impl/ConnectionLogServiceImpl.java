package kr.or.kids.domain.ca.connecionlog.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import kr.or.kids.domain.ca.connecionlog.mapper.ConnectionLogMapper;
import kr.or.kids.domain.ca.connecionlog.service.ConnectionLogService;
import kr.or.kids.domain.ca.connecionlog.vo.ConnectionLogDataResVO;
import kr.or.kids.domain.ca.connecionlog.vo.ConnectionLogInsertReqVO;
import kr.or.kids.global.system.common.ApiResultCode;
import kr.or.kids.global.system.common.vo.ApiPrnDto;
import kr.or.kids.global.util.DrugsafeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class ConnectionLogServiceImpl implements ConnectionLogService {

    @Autowired
    private ConnectionLogMapper connectionLogMapper;


    @Override
    public ApiPrnDto list(int pageNum,int pageSize) {
        ApiPrnDto apiPrnDto = new ApiPrnDto(ApiResultCode.SUCCESS);
        HashMap<String, Object> data = new HashMap<>();

        try {
            log.info("=== 페이징 요청: pageNum={}, pageSize={} ===", pageNum, pageSize);

            // ⭐ PageHelper는 반드시 쿼리 실행 전에 호출해야 함
            PageHelper.startPage(pageNum, pageSize);

            // Mapper의 list() 메서드 호출 (PageHelper가 자동으로 페이징 쿼리 적용)
            List<ConnectionLogDataResVO> list = connectionLogMapper.list();

            log.info("=== DB 조회 결과: userList.size()={} ===", list.size());

            // PageInfo로 감싸서 페이징 정보 추출
            PageInfo<ConnectionLogDataResVO> pageInfo = new PageInfo<>(list);

            // 마스킹처리
            List<ConnectionLogDataResVO> pageList = pageInfo.getList();
            // 페이징 정보 및 데이터 설정
            data.put("pageNum", pageInfo.getPageNum());           // 현재 페이지 번호
            data.put("pageSize", pageInfo.getPageSize());         // 페이지당 개수
            data.put("totalPages", pageInfo.getPages());          // 총 페이지 수
            data.put("totalCount", pageInfo.getTotal());          // 전체 데이터 개수
            data.put("list", pageList);                               // 현재 페이지 데이터
            data.put("isFirstPage", pageInfo.isIsFirstPage());    // 첫 페이지 여부
            data.put("isLastPage", pageInfo.isIsLastPage());      // 마지막 페이지 여부
            data.put("hasPreviousPage", pageInfo.isHasPreviousPage()); // 이전 페이지 존재 여부
            data.put("hasNextPage", pageInfo.isHasNextPage());    // 다음 페이지 존재 여부
            // 데이터가 없어도 성공으로 처리 (HTTP 200)
            if (pageList.size() == 0) {
                apiPrnDto.setMsg("조회된 데이터가 없습니다.");
            } else {
                apiPrnDto.setMsg("사용자 목록 조회 완료");
            }

            log.info("사용자 목록 조회 완료: {} 건", list.size());

        } catch (Exception e) {
            log.error("사용자 목록 조회 실패", e);
            apiPrnDto = new ApiPrnDto(ApiResultCode.SYSTEM_ERROR);
            apiPrnDto.setMsg("사용자 목록 조회 중 오류가 발생했습니다.");
        }

        apiPrnDto.setData(data);
        return apiPrnDto;
    }

    /**
     * 사용자정보등록
     * @param param
     * @return
     */
    @Transactional
    public ApiPrnDto insert(ConnectionLogInsertReqVO insertVO) {

        ApiPrnDto result             = new ApiPrnDto();
        HashMap<String, Object> bizData = new HashMap<>();

        try {
            
            if (!StringUtils.hasLength(insertVO.getSrvcUserId())) {
                throw new RuntimeException("srvcUserId 파라미터가 누락되었습니다.");
            }

            /**
             * BizProc
             */
            Long connectLogNextId = connectionLogMapper.nextConnectionLogReq();

            DrugsafeUtil util = new DrugsafeUtil();
            String yyyyMMddHHmmss = util.dateFromat("yyyyMMddHHmmss");
            String HHmmss = util.dateFromat("HHmmss");

            insertVO.setSessLogSn(connectLogNextId);
            insertVO.setCntnOcrnDt(yyyyMMddHHmmss);

            // 로그인 아웃 여부 1 로그인 2 로그아웃
            if (insertVO.getCntnSeNo().equals("2")){
                insertVO.setSessExpryPrnmntHr(HHmmss);
            }else{
                insertVO.setTokenCrtHr(HHmmss);
            }

            connectionLogMapper.insert(insertVO);

            result.setCode("0");
            result.setMsg("로그인 접속정보가  등록되었습니다.");
            bizData.put("cntnLogSn", connectLogNextId);

        } catch(Exception e) {
            result = DrugsafeUtil.getApiPrnDto("-1", e.toString());
        }

        result.setData(bizData);
        return result;
    }


}
