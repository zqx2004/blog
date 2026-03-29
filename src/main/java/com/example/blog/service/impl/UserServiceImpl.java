package com.example.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blog.common.result.Result;
import com.example.blog.common.util.JwtUtil;
import com.example.blog.entity.User;
import com.example.blog.mapper.UserMapper;
import com.example.blog.service.UserService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private JwtUtil jwtUtil;

    // 注册
    @Override
    public Result register(User user) {
        // 1. 判断用户名是否已存在
        User exist = lambdaQuery()
                .eq(User::getUsername, user.getUsername())
                .one();
        if (exist != null) {
            return Result.error("用户名已存在");
        }

        // 2. 密码加密（重要！不能明文存）
        String newPwd = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(newPwd);

        // 3. 保存
        save(user);
        return Result.success("注册成功");
    }

    // 登录
    @Override
    public Result login(User user) {
        // 1. 根据用户名查用户
        User dbUser = lambdaQuery()
                .eq(User::getUsername, user.getUsername())
                .one();
        if (dbUser == null) {
            return Result.error("用户不存在");
        }

        // 2. 对比密码（加密对比）
        if (!BCrypt.checkpw(user.getPassword(), dbUser.getPassword())) {
            return Result.error("密码错误");
        }

        // 3. 生成 JWT token
        String token = jwtUtil.generateToken(dbUser.getId());

        return Result.success(token);
    }
}