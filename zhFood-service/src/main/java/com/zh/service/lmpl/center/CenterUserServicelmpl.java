package com.zh.service.lmpl.center;

import com.zh.mapper.UsersMapper;
import com.zh.pojo.Users;
import com.zh.pojo.bo.center.CenterUserBO;
import com.zh.service.center.CenterUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class CenterUserServicelmpl implements CenterUserService {
    @Autowired
    private UsersMapper usersMapper;
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Users queryUserInfo(String userId) {
        Users user = usersMapper.selectByPrimaryKey(userId);
        user.setPassword(null);
        return user;
    }
@Transactional(propagation =Propagation.REQUIRED )
    @Override
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO) {
        Users updateUser =new Users();
         BeanUtils.copyProperties(centerUserBO,updateUser);
         updateUser.setId(userId);
         updateUser.setUpdatedTime(new Date());
        usersMapper.updateByPrimaryKeySelective(updateUser);
        return queryUserInfo(userId);
    }
    @Transactional(propagation =Propagation.REQUIRED )
    @Override
    public Users updateUserFace(String userId, String faceUrl) {
        Users updateUser =new Users();
        updateUser.setId(userId);
        updateUser.setFace(faceUrl);
        updateUser.setUpdatedTime(new Date());
        usersMapper.updateByPrimaryKeySelective(updateUser);
        return queryUserInfo(userId);
    }
}
