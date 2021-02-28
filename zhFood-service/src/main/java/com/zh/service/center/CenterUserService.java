package com.zh.service.center;

import com.zh.pojo.Users;
import com.zh.pojo.bo.center.CenterUserBO;

public interface CenterUserService {
/*查询用户个人信息
* */
public Users queryUserInfo(String userId);
/*
* 修改用户个人信息
* */
public Users updateUserInfo(String userId, CenterUserBO centerUserBO);
    /*
     * 修改用户头像
     * */
public Users updateUserFace(String userId, String faceUrl);
}
