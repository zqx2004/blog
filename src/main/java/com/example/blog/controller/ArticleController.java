package com.example.blog.controller;

import com.example.blog.common.result.Result;
import com.example.blog.entity.Article;
import com.example.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping("/add")
    public Result add(@RequestBody Article article) {
        return articleService.add(article);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Article article) {
        return articleService.update(article);
    }

    @GetMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        return articleService.delete(id);
    }

    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id) {
        return articleService.getArticleById(id);
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size) {
        return articleService.listPage(page, size);
    }
}
