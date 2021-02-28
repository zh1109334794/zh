package com.zh.controller.center;

import com.zh.controller.BaseController;
import com.zh.pojo.Users;
import com.zh.pojo.bo.center.CenterUserBO;
import com.zh.resource.FileUpload;
import com.zh.service.center.CenterUserService;
import com.zh.utils.CookieUtils;
import com.zh.utils.DateUtil;
import com.zh.utils.IMOOCJSONResult;
import com.zh.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "用户信息接口",tags = "用户信息的一些相关接口")
@RestController
@RequestMapping("userInfo")
public class CenterUserController extends BaseController {
    @Autowired
    private CenterUserService centerUserService;
    @Autowired
    private FileUpload fileUpload;
    @ApiOperation(value = "修改用户头像", notes = "修改用户头像", httpMethod = "POST")
    @PostMapping("uploadFace")
    public IMOOCJSONResult update(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "file", value = "用户头像", required = true)
                    MultipartFile file,
            HttpServletRequest request, HttpServletResponse response)
    {
//        定义头像保存地址
//        String fileSpace=IMAGE_USER_FACE_LOCATION;
        String fileSpace=fileUpload.getImageUserFaceLocation();
//        在路径上为每一个用户添加用户id，用来区分
       String upLoadPathPrefix= File.separator+userId;
//       开始上传文件
       if (file!=null){
           FileOutputStream fileOutputStream=null;
//              获取文件上传的文件名称
           String fileName= file.getOriginalFilename();
           try {
               if (StringUtils.isNotBlank(fileName)){
    //                 文件重命名
                   String fileNameArr[]=fileName.split("\\.");
    //               获取文件的后缀名
                   String suffix =fileNameArr[fileNameArr.length-1];
//                 判断上传图片的的格式，防止后门
                   if (!suffix.equalsIgnoreCase("png") &&
                           !suffix.equalsIgnoreCase("jpg") &&
                           !suffix.equalsIgnoreCase("jpeg") ) {
                       return IMOOCJSONResult.errorMsg("图片格式不正确！");
                   }
    //               文件名称重组 覆盖式上传。
                   String newFileName="face-"+userId+"."+suffix;
    //               上传的头像最终保存位置
                   String finalFacePath=fileSpace+upLoadPathPrefix+File.separator+newFileName;
                   // 用于提供给web服务访问的地址
                   upLoadPathPrefix += ("/" + newFileName);
                   File outFile=new File(finalFacePath);
                   if (outFile.getParentFile()!=null){
    //                   创建文件夹
                       outFile.getParentFile().mkdirs();
                   }
    //               文件输出保存到目录
                   fileOutputStream=new FileOutputStream(outFile);
                   InputStream inputStream =file.getInputStream();
                   IOUtils.copy(inputStream,fileOutputStream);
               }
           } catch (IOException e) {
               e.printStackTrace();
           }finally {
               try {
                   if (fileOutputStream!=null){
                       fileOutputStream.flush();
                       fileOutputStream.close();
                   }
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }

       }else{
           return IMOOCJSONResult.errorMsg("文件不能为空！");
       }

        // 获取图片服务地址
        String imageServerUrl = fileUpload.getImageServerUrl();

        // 由于浏览器可能存在缓存的情况，所以在这里，我们需要加上时间戳来保证更新后的图片可以及时刷新
        String finalUserFaceUrl = imageServerUrl + upLoadPathPrefix
                + "?t=" + DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);

        // 更新用户头像到数据库
        Users userResult = centerUserService.updateUserFace(userId, finalUserFaceUrl);

        userResult = setNullProperty(userResult);
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(userResult), true);

        // TODO 后续要改，增加令牌token，会整合进redis，分布式会话
         return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", httpMethod = "POST")
    @PostMapping("update")
    public IMOOCJSONResult update(
            @ApiParam(name = "UserId", value = "用户id", required = true)
            @RequestParam String userId, @RequestBody @Valid CenterUserBO centerUserBO, BindingResult result,
            HttpServletRequest request,
            HttpServletResponse response)
      {
          if (result.hasErrors()){
               Map<String,String>errorMap= getErrors(result);
               return IMOOCJSONResult.errorMap(errorMap);
          }
        Users userResult = centerUserService.updateUserInfo(userId, centerUserBO);
        userResult = setNullProperty(userResult);
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(userResult), true);
        // TODO 后续要改，增加令牌token，会整合进redis，分布式会话
        return IMOOCJSONResult.ok();
    }
//    把不必要的用户信息隐藏，设置为null返回出去
    private Users setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }
//    获取bo的错误信息放入map集合中
    private Map<String,String> getErrors(BindingResult result){
        Map<String,String> map =new HashMap<>();
        List <FieldError> errorList=result.getFieldErrors();
        for (FieldError error:errorList){
//            发生验证错误对应的某一个属性
            String errorField=error.getField();
//            发生验证错误对应的信息
           String errorMessage= error.getDefaultMessage();
           map.put(errorField,errorMessage);
        }
        return map;
    }
}