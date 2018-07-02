package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.SystemDictionary;
import cn.wolfcode.p2p.base.query.SystemDictionaryQueryObject;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by wolfcode on 2018/03/14 0014.
 */
public interface ISystemDictionaryService {
    int save(SystemDictionary systemDictionary);
    int update(SystemDictionary systemDictionary);
    SystemDictionary get(Long id);
    List<SystemDictionary> selectAll();

    PageInfo queryPage(SystemDictionaryQueryObject qo);

    void saveOrUpdate(SystemDictionary systemDictionary);
}
