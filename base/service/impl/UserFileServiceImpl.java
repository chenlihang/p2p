package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.SystemDictionaryItem;
import cn.wolfcode.p2p.base.domain.UserFile;
import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.mapper.UserFileMapper;
import cn.wolfcode.p2p.base.query.QueryObject;
import cn.wolfcode.p2p.base.service.IUserFileService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.util.UserContext;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by wolfcode on 2018/03/15 0015.
 */
@Service@Transactional
public class UserFileServiceImpl implements IUserFileService {
    @Autowired
    private UserFileMapper userFileMapper;
    @Autowired
    private IUserinfoService userinfoService;
    @Override
    public int save(UserFile userFile) {
        return userFileMapper.insert(userFile);
    }

    @Override
    public int update(UserFile userFile) {
        return userFileMapper.updateByPrimaryKey(userFile);
    }

    @Override
    public UserFile get(Long id) {
        return userFileMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo queryPage(QueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());
        List list = userFileMapper.selectList(qo);
        return new PageInfo(list);
    }

    @Override
    public void apply(String imagePath) {
        UserFile userFile = new UserFile();
        userFile.setApplier(UserContext.getCurrent());
        userFile.setApplyTime(new Date());
        userFile.setImage(imagePath);
        userFile.setState(UserFile.STATE_NORMAL);
        this.save(userFile);
    }

    @Override
    public List<UserFile> queryUnSelectFileTypeList() {
        return userFileMapper.queryUnSelectFileTypeList(UserContext.getCurrent().getId());
    }

    @Override
    public void selectType(Long[] ids, Long[] fileTypes) {
        if(ids!=null && fileTypes!=null && ids.length==fileTypes.length){
            //遍历集合
            UserFile uf;
            SystemDictionaryItem fileType;
            for(int i=0;i<ids.length;i++){
                uf = this.get(ids[i]);
                //只能操作自己的数据
                if(uf.getApplier().getId().equals(UserContext.getCurrent().getId())){
                    fileType = new SystemDictionaryItem();
                    fileType.setId(fileTypes[i]);
                    uf.setFileType(fileType);
                    this.update(uf);
                }
            }
        }
    }

    @Override
    public List<UserFile> queryFileTypeList(boolean isSelectFileType) {
        return userFileMapper.queryFileTypeList(isSelectFileType,UserContext.getCurrent().getId());
    }

    @Override
    public void audit(Long id, int score, int state, String remark) {
        //1.通过id获取对应的userFile对象,合理性判断
        UserFile userFile = this.get(id);
        if(userFile!=null && userFile.getState()==UserFile.STATE_NORMAL){
            //2.设置审核人,审核时间,审核备注
            userFile.setAuditor(UserContext.getCurrent());
            userFile.setAuditTime(new Date());
            userFile.setRemark(remark);
            //3.如果审核通过
            if(state==UserFile.STATE_PASS){
                //      改变状态
                userFile.setState(UserFile.STATE_PASS);
                userFile.setScore(score);
                //      给用户添加对应的分数userinfo有score字段
                Userinfo userinfo = userinfoService.get(userFile.getApplier().getId());
                userinfo.setScore(userinfo.getScore()+score);
                userinfoService.update(userinfo);
            }else{
                //4.如果审核失败
                userFile.setState(UserFile.STATE_REJECT);
                //      改变状态
            }
            this.update(userFile);

        }

    }

    @Override
    public List<UserFile> queryListByUserId(Long logininfoId) {
        return this.userFileMapper.queryListByUserId(logininfoId);
    }
}
