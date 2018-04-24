package com.example.service;

import com.example.dao.ArticleDaoImpl;
import com.example.dao.IArticleDao;
import com.example.po.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Created by samsung on 2017/10/30.
 */
@Service
@Transactional
public class ArticleServiceImpl {
    @Autowired
    private IArticleDao dao;

    //必须为public 否则无法调用
    public Page<Article> queryAll(Pageable pageable, Integer rid) {
        return dao.queryAll(pageable, rid);
    }

    public void delete(Integer id) {
        dao.delete(id, id);
    }

    public void deletec(Integer id,Integer rid){
        dao.deletec(id,rid);
    }
    @Autowired
    private ArticleDaoImpl adao;

    public Map<String, Object> findArticleById(Integer id) {
        return adao.findArticleById(id);
    }
    public Article save(Article article){
        return dao.save(article);
    }
}