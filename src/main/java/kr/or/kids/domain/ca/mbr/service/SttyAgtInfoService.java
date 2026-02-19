package kr.or.kids.domain.ca.mbr.service;

import kr.or.kids.domain.ca.mbr.vo.SttyAgtInfoDVO;
import kr.or.kids.domain.ca.mbr.vo.SttyAgtInfoPVO;
import kr.or.kids.domain.ca.mbr.vo.SttyAgtInfoRVO;

public interface SttyAgtInfoService
{
    /**
     * 대국민포털_법정대리인정보기본 정보 조회 
     *
     * @param sttyAgtInfoPVO 조회용 파라메터 정보 
     * @return 조회된 대국민포털_법정대리인정보기본 
     */
    public SttyAgtInfoRVO getSttyAgtInfo(SttyAgtInfoPVO sttyAgtInfoPVO);

    /**
     * 대국민포털_법정대리인정보기본 정보 입력 
     *
     * @param sttyAgtInfoPVO 입력할 대국민포털_법정대리인정보기본 정보 
     * @return 입력된 건수 
     */
    public int insertSttyAgtInfo(SttyAgtInfoPVO sttyAgtInfoPVO);

    /**
     * 대국민포털_법정대리인정보기본 정보 수정 
     *
     * @param sttyAgtInfoPVO 수정할 대국민포털_법정대리인정보기본 정보 
     * @return 수정된 건수 
     */
    public int updateSttyAgtInfo(SttyAgtInfoPVO sttyAgtInfoPVO);

    /**
     * 대국민포털_법정대리인정보기본 정보 저장 
     * <p>
     * 기존 데이터가 존재하지 않으면 입력하고, 존재하면 수정한다.
     * </p>
     *
     * @param sttyAgtInfoPVO 저장할 대국민포털_법정대리인정보기본 정보 
     * @return 저장된 건수 
     */
    public int saveSttyAgtInfo(SttyAgtInfoPVO sttyAgtInfoPVO);

    /**
     * 대국민포털_법정대리인정보기본 정보 삭제 
     *
     * @param sttyAgtInfoDVO 삭제용 파라메터 정보 
     * @return 삭제된 건수 
     */
    public int deleteSttyAgtInfo(SttyAgtInfoDVO sttyAgtInfoDVO);
}
