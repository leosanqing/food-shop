package com.leosanqing.service.impl;

import com.leosanqing.enums.Sex;
import com.leosanqing.mapper.UsersMapper;
import com.leosanqing.pojo.Users;
import com.leosanqing.pojo.bo.UserBO;
import com.leosanqing.service.UserService;
import com.leosanqing.utils.DateUtil;
import com.leosanqing.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

/**
 * @Author: leosanqing
 * @Date: 2019-12-06 00:16
 */
@Service
public class UserServiceImpl implements UserService {

    private static final String FACE_PATH = "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAllFXAAAclhVPdSg994.png";

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("username", username);
        return usersMapper.selectOneByExample(userExample) != null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Users createUser(UserBO userBO) {

        Users users = new Users();
        users.setId(sid.nextShort());
        users.setUsername(userBO.getUsername());
        try {
            users.setPassword(MD5Utils.getMD5Str(userBO.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        users.setBirthday(DateUtil.stringToDate("1900-01-01"));
        users.setFace(FACE_PATH);
        users.setNickname(userBO.getUsername());
        users.setSex(Sex.SECRET.type);
        users.setCreatedTime(new Date());
        users.setUpdatedTime(new Date());


        usersMapper.insert(users);
        return users;
    }


    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Users queryUsersForLogin(String username, String password){
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",username);
        criteria.andEqualTo("password",password);

        Users users = usersMapper.selectOneByExample(example);
        return users;

    }
}
