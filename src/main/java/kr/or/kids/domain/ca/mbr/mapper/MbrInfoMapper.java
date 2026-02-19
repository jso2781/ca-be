package kr.or.kids.domain.ca.mbr.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.or.kids.domain.ca.mbr.vo.MbrInfoDVO;
import kr.or.kids.domain.ca.mbr.vo.MbrInfoPVO;
import kr.or.kids.domain.ca.mbr.vo.MbrInfoRVO;

import java.util.List;

@Mapper
public interface MbrInfoMapper
{

    /**
     * 대국민포털_회원정보기본 목록 조회
     *
     * @param mbrInfoPVO 조회용 파라메터 정보
     * @return 조회된 대국민포털_회원정보기본
     */
    List<MbrInfoRVO> list();

    /**
     * 대국민포털_회원정보기본 기존 아이디, 이메일 존재여부 조회
     * 
     * @param mbrInfoPVO 조회용 파라메터 정보
     * @return 조회된 대국민포털_회원정보기본
     */
    public int checkMbrInfo(MbrInfoPVO mbrInfoPVO);

    /**
     * 대국민포털_회원정보기본 정보 조회 
     *
     * @param mbrInfoPVO 조회용 파라메터 정보 
     * @return 조회된 대국민포털_회원정보기본 
     */
    public MbrInfoRVO getMbrInfo(MbrInfoPVO mbrInfoPVO);

    /**
     * 대국민포털_회원정보기본 정보 입력 
     *
     * @param mbrInfoPVO 입력할 대국민포털_회원정보기본 정보 
     * @return 입력된 건수 
     */
    public int insertMbrInfo(MbrInfoPVO mbrInfoPVO);

    /**
     * 대국민포털_회원정보기본 정보 수정 
     *
     * @param mbrInfoPVO 수정할 대국민포털_회원정보기본 정보 
     * @return 수정된 건수 
     */
    public int updateMbrInfo(MbrInfoPVO mbrInfoPVO);

    /**
     * 대국민포털_회원정보기본 정보 저장 
     * <p>
     * 기존 데이터가 존재하지 않으면 입력하고, 존재하면 수정한다.
     * </p>
     *
     * @param mbrInfoPVO 저장할 대국민포털_회원정보기본 정보 
     * @return 저장된 건수 
     */
    public int saveMbrInfo(MbrInfoPVO mbrInfoPVO);

    /**
     * 대국민포털_회원정보기본 정보 삭제 
     *
     * @param mbrInfoDVO 삭제용 파라메터 정보 
     * @return 삭제된 건수 
     */
    public int deleteMbrInfo(MbrInfoDVO mbrInfoDVO);
}
