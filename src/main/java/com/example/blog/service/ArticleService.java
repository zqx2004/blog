package com.example.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog.common.result.Result;
import com.example.blog.entity.Article;

public interface ArticleService extends IService<Article> {
    Result add(Article article);
    Result update(Article article);
    Result delete(Long id);
    Result getArticleById(Long id); // 这里改名
    Result listPage(Integer page, Integer size);
}