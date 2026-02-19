package kr.or.kids.domain.ca.mbr.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.kids.domain.ca.mbr.mapper.SttyAgtInfoMapper;
import kr.or.kids.domain.ca.mbr.service.SttyAgtInfoService;
import kr.or.kids.domain.ca.mbr.vo.SttyAgtInfoDVO;
import kr.or.kids.domain.ca.mbr.vo.SttyAgtInfoPVO;
import kr.or.kids.domain.ca.mbr.vo.SttyAgtInfoRVO;

@Service
public class SttyAgtInfoServiceImpl implements SttyAgtInfoService
{
    @Autowired
    private SttyAgtInfoMapper sttyAgtInfoMapper;

    @Override
    public SttyAgtInfoRVO getSttyAgtInfo(SttyAgtInfoPVO sttyAgtInfoPVO)
    {
        return sttyAgtInfoMapper.getSttyAgtInfo(sttyAgtInfoPVO);
    }

    @Override
    public int insertSttyAgtInfo(SttyAgtInfoPVO sttyAgtInfoPVO)
    {
        return sttyAgtInfoMapper.insertSttyAgtInfo(sttyAgtInfoPVO);
    }

    @Override
    public int updateSttyAgtInfo(SttyAgtInfoPVO sttyAgtInfoPVO)
    {
        return sttyAgtInfoMapper.updateSttyAgtInfo(sttyAgtInfoPVO);
    }

    @Override
    public int saveSttyAgtInfo(SttyAgtInfoPVO sttyAgtInfoPVO)
    {
        return sttyAgtInfoMapper.saveSttyAgtInfo(sttyAgtInfoPVO);
    }

    @Override
    public int deleteSttyAgtInfo(SttyAgtInfoDVO sttyAgtInfoDVO)
    {
        return sttyAgtInfoMapper.deleteSttyAgtInfo(sttyAgtInfoDVO);
    }
}
