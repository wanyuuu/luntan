package com.example.dao;

import com.example.po.Bbsuser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by samsung on 2017/10/28.
 * 对Bbsuser 这张表进行操作 主键类型 遵循API规范
 */
public interface IUserDao extends CrudRepository<Bbsuser,Integer> {
    @Query("select c from Bbsuser c where username=:u and password=:p")
    Bbsuser login(@Param("u")String username,@Param("p") String password);

    @Query("select c from Bbsuser c where userid=:id")
    Bbsuser getPic(@Param("id") Integer id);
    Bbsuser save(Bbsuser user);
}
