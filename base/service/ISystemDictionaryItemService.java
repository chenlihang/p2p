package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.SystemDictionary;
import cn.wolfcode.p2p.base.domain.SystemDictionaryItem;
import cn.wolfcode.p2p.base.query.SystemDictionaryItemQueryObject;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by wolfcode on 2018/03/14 0014.
 */
public interface ISystemDictionaryItemService {
    int save(SystemDictionaryItem systemDictionaryItem);
    int update(SystemDictionaryItem systemDictionaryItem);
    SystemDictionaryItem get(Long id);

    PageInfo queryPage(SystemDictionaryItemQueryObject qo);

    void saveOrUpdate(SystemDictionaryItem systemDictionaryItem);

    List<SystemDictionaryItem> queryListByParentSn(String sn);
}
