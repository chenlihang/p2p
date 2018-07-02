package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.UserFile;
import cn.wolfcode.p2p.base.query.QueryObject;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by wolfcode on 2018/03/15 0015.
 */
public interface IUserFileService {
    int save(UserFile userFile);
    int update(UserFile userFile);
    UserFile get(Long id);
    PageInfo queryPage(QueryObject qo);

    /**
     * 风控材料申请
     * @param imagePath
     */
    void apply(String imagePath);

    List<UserFile> queryUnSelectFileTypeList();

    void selectType(Long[] ids, Long[] fileTypes);

    /**
     * 查询风控记录集合
     * @param isSelectFileType  true:查询已经选择了风控类型的记录
     *                          false:查询没有选择了风控类型的记录
     * @return
     */
    List<UserFile> queryFileTypeList(boolean isSelectFileType);

    /**
     * 风控材料审核
     * @param id
     * @param score
     * @param state
     * @param remark
     */
    void audit(Long id, int score, int state, String remark);

    /**
     * 根据id获取对应用户已经审核通过的记录
     * @param id
     * @return
     */
    List<UserFile> queryListByUserId(Long logininfoId);
}
