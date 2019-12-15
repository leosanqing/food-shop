package com.leosanqing.controller.center;

import com.leosanqing.pojo.Users;
import com.leosanqing.pojo.bo.center.CenterUserBO;
import com.leosanqing.service.center.CenterUserService;
import com.leosanqing.utils.CookieUtils;
import com.leosanqing.utils.JSONResult;
import com.leosanqing.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: leosanqing
 * @Date: 2019-12-15 20:35
 * @Package: com.leosanqing.controller.center
 * @Description: TODO
 */
@Api
@RestController
@RequestMapping("userInfo")
public class CenterUserController {
    public static final String USER_FACE_IMG_LOCATION =
            File.separator + "Users" +
                    File.separator + "zhuerchong" +
                    File.separator + "Desktop" +
                    File.separator + "code" +
                    File.separator + "idea" +
                    File.separator + "foodie-dev" +
                    File.separator + "img";


    @Autowired
    private CenterUserService centerUserService;

    @PostMapping("update")
    @ApiOperation(value = "更新用户信息", notes = "更新用户信息", httpMethod = "POST")
    public JSONResult updateUserInfo(
            @ApiParam(name = "userId", value = "用户id")
            @RequestParam String userId,
            @ApiParam(name = "centerUserBO", value = "用户中心bo")
            @RequestBody @Valid CenterUserBO centerUserBO,
            BindingResult result,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("用户名id为空");
        }

        if (result.hasErrors()) {

            final Map<String, String> errorMap = getErrors(result);
            return JSONResult.errorMap(errorMap);
        }

        final Users users = centerUserService.updateUserInfo(userId, centerUserBO);

        setNullProperty(users);
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(users), true);

        //TODO 后续增加令牌 整合进redis
        return JSONResult.ok(users);
    }


    @PostMapping("uploadFace")
    @ApiOperation(value = "查询用户信息", notes = "查询用户信息", httpMethod = "POST")
    public JSONResult queryUserInfo(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "file", value = "用户头像", required = true)
                    MultipartFile file

    ) {
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("用户名id为空");
        }
        String userFaceImgPrefix = File.separator + userId;

        if (file == null) {
            return JSONResult.errorMsg("文件不能为空");
        }

        final String filename = file.getOriginalFilename();
        if (StringUtils.isNotBlank(filename)) {
            final String[] split = StringUtils.split(filename, "\\.");
            if (split != null) {
                String newFileName = "face-" + userId + "." + split[split.length - 1];

                // 文件最终保存的路径
                String finalPath = USER_FACE_IMG_LOCATION + userFaceImgPrefix + File.pathSeparator + newFileName;

                final File outFile = new File(finalPath);
                final File parent = outFile.getParentFile();
                if(parent != null){
                    // 创建文件夹
                    parent.mkdirs();
                }

                try (BufferedInputStream bin = new BufferedInputStream(file.getInputStream());
                     BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(outFile))) {
                    int b;
                    while ((b = bin.read()) != -1) {
                        bout.write(b);
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        return JSONResult.ok();
    }

    /**
     * @param result
     * @return
     */
    private Map<String, String> getErrors(BindingResult result) {

        final HashMap<String, String> map = new HashMap<>();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            String errorField = fieldError.getField();
            final String defaultMessage = fieldError.getDefaultMessage();
            map.put(errorField, defaultMessage);
        }


        return map;
    }


    /**
     * 将用户的部分信息设置为空，保护隐私
     *
     * @param users
     * @return
     */
    private Users setNullProperty(Users users) {
        users.setUpdatedTime(null);
        users.setCreatedTime(null);
        users.setBirthday(null);
        users.setMobile(null);
        users.setRealname(null);
        users.setEmail(null);
        users.setPassword(null);

        return users;
    }
}
