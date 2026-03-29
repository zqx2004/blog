package com.example.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blog.common.result.Result;
import com.example.blog.common.util.UserHolder;
import com.example.blog.entity.Article;
import com.example.blog.mapper.ArticleMapper;
import com.example.blog.service.ArticleService;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Override
    public Result add(Article article) {
        Long userId = UserHolder.getUserId();
        article.setUserId(userId);
        save(article);
        return Result.success("发布成功");
    }

    @Override
    public Result update(Article article) {
        Long userId = UserHolder.getUserId();
        Article old = getById(article.getId()); // 这里调用父类方法，正常

        if (old == null || !old.getUserId().equals(userId)) {
            return Result.error("无权限修改");
        }

        updateById(article);
        return Result.success("修改成功");
    }

    @Override
    public Result delete(Long id) {
        Long userId = UserHolder.getUserId();
        Article article = getById(id);

        if (article == null || !article.getUserId().equals(userId)) {
            return Result.error("无权限删除");
        }

        removeById(id);
        return Result.success("删除成功");
    }

    // 👇 改个名字！不要叫 getById！
    @Override
    public Result getArticleById(Long id) {
        Article article = getById(id);
        return Result.success(article);
    }

    @Override
    public Result listPage(Integer page, Integer size) {
        Page<Article> pageResult = lambdaQuery().page(new Page<>(page, size));
        return Result.success(pageResult);
    }
}