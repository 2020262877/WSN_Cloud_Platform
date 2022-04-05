package com.gong.mapper;

import com.gong.pojo.User;

import java.util.List;

public interface UserMapper {
    //增加用户
    int addUser(User user);

    //删除用户
    int deleteUser(int id);

    //更新用户
    int updateUser(User user);

    //修改密码
    int updatePassword(User user);

    //通过id查询用户
    User queryUserById(int id);

    //通过username查询用户
    User queryUserByUserName(String username);

    //通过usertype查询用户
    List<User> queryUserByUserType(String usertype);

    //查询全部用户
    List<User> queryUserList();

    //通过用户名模糊查询用户
    List<User> getUserLike(String value);

    //查询数据库中是否存在邮箱
    User mailExist(String mail);
}
