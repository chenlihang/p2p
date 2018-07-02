package cn.wolfcode.p2p.bussiness.mapper;

import cn.wolfcode.p2p.bussiness.domain.Bid;
import org.apache.ibatis.annotations.Param;

public interface BidMapper {

    int insert(Bid record);

    Bid selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Bid record);

    void updateState(@Param("bidRequestId") Long bidRequestId, @Param("state") int state);
}