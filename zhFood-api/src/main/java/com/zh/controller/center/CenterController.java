package com.zh.controller.center;

import com.zh.pojo.Users;
import com.zh.service.center.CenterUserService;
import com.zh.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "center--用户中心",tags = "用户中心的一些相关接口")
@RestController
@RequestMapping("center")
public class CenterController {

    @Autowired
    private CenterUserService centerUserService;

    @ApiOperation(value = "获取用户信息", notes = "获取用户信息", httpMethod = "GET")
    @GetMapping("userInfo")
    public IMOOCJSONResult userInfo(
            @ApiParam(name = "UserId", value = "用户id", required = true)
            @RequestParam String userId) {
        Users user = centerUserService.queryUserInfo(userId);
        return IMOOCJSONResult.ok(user);
    }



}