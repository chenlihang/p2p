package cn.wolfcode.p2p.base.mapper;

import cn.wolfcode.p2p.base.domain.UserFile;
import cn.wolfcode.p2p.base.query.QueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserFileMapper {

    int insert(UserFile record);

    UserFile selectByPrimaryKey(Long id);

    List<UserFile> selectList(QueryObject qo);

    int updateByPrimaryKey(UserFile record);

    List<UserFile> queryUnSelectFileTypeList(Long applierId);

    List<UserFile> queryFileTypeList(@Param("isSelectFileType") boolean isSelectFileType, @Param("applierId") Long applierId);

    List<UserFile> queryListByUserId(Long logininfoId);
}