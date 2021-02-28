package com.zh.service;
//判断用户名是否为空，如果不为空则返回ture，空则返回false

import com.zh.pojo.Users;
import com.zh.pojo.bo.UserBo;

public interface UserService {
    /*  查询用户，名是否存在*/
    public boolean queryUsernameIsExist(String username);
    /*  注册用户*/
    public Users createUser(UserBo userBo);
    /*  用户登录*/
    public Users queryUserForLogin(String username, String password);
}
