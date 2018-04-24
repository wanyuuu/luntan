package com.example.dao;

import com.example.po.Article;
import com.sun.tools.corba.se.idl.InterfaceGen;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by samsung on 2017/10/30.
 */
public interface IArticleDao extends CrudRepository<Article,Integer> {
    @Query("select c from Article c where rootid=:rid")
    Page<Article> queryAll(Pageable pageable, @Param("rid") Integer rid);

    @Modifying
    @Query("delete from Article where id=:id or rootid=:rid")//删除主帖
    public void delete(@Param("id") Integer id,@Param("rid") Integer rid );
    @Modifying
    @Query("delete from Article where id=:id and rootid=:rid")//删除从帖
    public void deletec(@Param("id") Integer id, @Param("rid")Integer rid);
}
