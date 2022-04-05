package com.gong.service.impl;

import com.gong.mapper.UserMapper;
import com.gong.pojo.User;
import com.gong.service.UserService;
import com.gong.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public int addUser(User user){
        user.setPassword(MD5.getInstance().getMD5(user.getPassword()));
        return userMapper.addUser(user);
    }

    //删除用户
    public int deleteUser(int id){
        return userMapper.deleteUser(id);
    }

    //更新用户
    public int updateUser(User user){
        user.setPassword(MD5.getInstance().getMD5(user.getPassword()));
        return userMapper.updateUser(user);
    }

    @Override
    public int updatePassword(User user) {
        return userMapper.updatePassword(user);
    }

    //查询一个用户
    public User queryUserById(int id){
        return userMapper.queryUserById(id);
    }

    //根据用户名查询
    @Override
    public User queryUserByUserName(String username) {
        return userMapper.queryUserByUserName(username);
    }

    //根据用户类型查询
    @Override
    public List<User> queryUserByUserType(String usertype) {
        return userMapper.queryUserByUserType(usertype);
    }

    //查询全部用户
    @Override
    public List<User> queryUserList(){
        return userMapper.queryUserList();
    }

    //通过用户名模糊查询用户
    @Override
    public List<User> getUserLike(String value) {
        return userMapper.getUserLike(value);
    }

    @Override
    public User mailExist(String mail) {
        return userMapper.mailExist(mail);
    }
}
