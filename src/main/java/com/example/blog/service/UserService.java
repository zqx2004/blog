package com.example.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog.common.result.Result;
import com.example.blog.entity.User;

public interface UserService extends IService<User> {
    Result register(User user);
    Result login(User user);
}

