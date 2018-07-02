package cn.wolfcode.p2p.base.mapper;

import cn.wolfcode.p2p.base.domain.SystemDictionaryItem;
import cn.wolfcode.p2p.base.query.SystemDictionaryItemQueryObject;
import java.util.List;

public interface SystemDictionaryItemMapper {

    int insert(SystemDictionaryItem record);

    SystemDictionaryItem selectByPrimaryKey(Long id);


    int updateByPrimaryKey(SystemDictionaryItem record);

    List queryList(SystemDictionaryItemQueryObject qo);

    List<SystemDictionaryItem> queryListByParentSn(String sn);
}