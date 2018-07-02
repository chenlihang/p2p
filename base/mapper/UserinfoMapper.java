package cn.wolfcode.p2p.base.mapper;

import cn.wolfcode.p2p.base.domain.Userinfo;
import java.util.List;

public interface UserinfoMapper {

    int insert(Userinfo record);

    Userinfo selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Userinfo record);
}