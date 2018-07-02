package cn.wolfcode.p2p.bussiness.mapper;

import cn.wolfcode.p2p.base.query.QueryObject;
import cn.wolfcode.p2p.bussiness.domain.BidRequest;
import java.util.List;

public interface BidRequestMapper {

    int insert(BidRequest record);

    BidRequest selectByPrimaryKey(Long id);

    List<BidRequest> selectList(QueryObject qo);

    int updateByPrimaryKey(BidRequest record);
}