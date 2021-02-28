package com.zh.service.lmpl;

import com.zh.enums.Sex;
import com.zh.mapper.UsersMapper;
import com.zh.pojo.Users;
import com.zh.pojo.bo.UserBo;
import com.zh.service.UserService;
import com.zh.utils.DateUtil;
import com.zh.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    public UsersMapper usersMapper;
    private static final String USER_FACE = "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png";
    @Autowired
    public Sid sid;
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean queryUsernameIsExist(String username) {
        Example userExample = new Example(Users.class);
        Example.Criteria userCriteria=userExample.createCriteria();
            userCriteria.andEqualTo("username",username);
       Users result= usersMapper.selectOneByExample(userExample);
        return result==null ? false:true;
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Users createUser (UserBo userBo){
            Users user=new Users();
        String user_id=sid.nextShort();
            user.setUsername(userBo.getUsername());
//            设置默认昵称与用户名同名
            user.setNickname(userBo.getUsername());
//           用MD5加密保存登陆密码
        try {
            user.setPassword(MD5Utils.getMD5Str(userBo.getPassword()) );
        } catch (Exception e) {
            e.printStackTrace();
        }
//        设置默认头像
        user.setFace(USER_FACE);
//        设置默认性别
        user.setSex(Sex.secret.type);
//        设置默认生日
        user.setBirthday(DateUtil.stringToDate("1997-01-10"));
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());
        user.setId(user_id );
//        把用户信息保存进数据库
        usersMapper.insert(user);
       return  user;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String password) {

        Example userExample = new Example(Users.class);
        Example.Criteria userCriteria=userExample.createCriteria();
        userCriteria.andEqualTo("username",username);userCriteria.andEqualTo("password",password);
      Users result =usersMapper.selectOneByExample(userExample);

        return result;
    }
}
