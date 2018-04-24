package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.example.config.FreemarkerUtils;
import com.example.po.Article;
import com.example.po.Bbsuser;
import com.example.service.ArticleServiceImpl;
import org.omg.CORBA.ValueMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.devtools.remote.server.Dispatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import sun.plugin.com.DispatchClient;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by samsung on 2017/10/31.
 */
@WebServlet(name = "ArticleController",urlPatterns = {"/article"},
        initParams = {@WebInitParam(name = "show",value = "show.ftl"),
                @WebInitParam(name = "welcome",value = "/welcome"),
            @WebInitParam(name = "showreply",value = "/article?action=queryid&id=")
        })
public class ArticleController extends HttpServlet {
    private Map<String,String> map=new HashMap<String,String>();
    @Autowired
    private ArticleServiceImpl service;
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action=req.getParameter("action");
        RequestDispatcher dispatcher;
        switch (action){
            case "queryall":
                String curpage=req.getParameter("cur");
                int pageSize=0;
                Map vmap=new HashMap<String,Object>();
                Bbsuser bbsuser=(Bbsuser) req.getSession().getAttribute("user");
                if(bbsuser!=null){  //防止游客产生空指针
                    pageSize=bbsuser.getPagenum();//获取用户设置的值
                    //新注册的用户需要设置页数 否则也会异常
                    vmap.put("msg","欢迎"+bbsuser.getUsername());
                    vmap.put("user",bbsuser);
                }else {//游客默认每页5帖
                    pageSize=5;
                }
                HashMap m=(HashMap)req.getAttribute("vmp");
                if(req.getAttribute("vmp")!=null){
                    vmap.put("msg",m.get("msg"));
                }
                Sort sort=new Sort(Sort.Direction.DESC,"id");
                Pageable page=new PageRequest(Integer.parseInt(curpage),pageSize,sort);//每页几行数据
                //p为从数据库获得的所有主帖
                Page<Article> p=service.queryAll(page,0);//Page继承的Slice接口中有判断首页尾页方法
                vmap.put("page",p);//游客也可看到帖子
                FreemarkerUtils.forward(resp,map.get("show"),vmap);
                break;
            case "del":
                String id=req.getParameter("id");
                service.delete(Integer.parseInt(id));
                dispatcher=req.getRequestDispatcher(map.get("welcome"));
                dispatcher.forward(req,resp);
            case "delc":
                id=req.getParameter("id");//从帖id
                String rootid=req.getParameter("rootid");//从帖所属主帖id
                service.deletec(Integer.parseInt(id),Integer.parseInt(rootid));
                dispatcher=req.getRequestDispatcher(map.get("showreply")+rootid);
                dispatcher.forward(req,resp);
            case "queryid": //回灌显示帖子窗体
                String rid=req.getParameter("id");
                Map<String,Object> map1=service.findArticleById(Integer.parseInt(rid));
                resp.setCharacterEncoding("utf-8");
                resp.setContentType("text/html");
                PrintWriter out=resp.getWriter();
                String json= JSON.toJSONString(map1,true);
                out.print(json);
                out.flush();
                out.close();
                break;
            case "add":
                Article art=new Article();
                Bbsuser bsuser=(Bbsuser) req.getSession().getAttribute("user");
                String content=req.getParameter("content");
                String title=req.getParameter("title");
                art.setContent(content);
                art.setTitle(title);
                art.setRootid(0);
                art.setDatetime(new Date(System.currentTimeMillis()));
                art.setUser(bsuser);
                service.save(art);//增加主帖
                dispatcher=req.getRequestDispatcher(map.get("welcome"));
                dispatcher.forward(req,resp);
                break;
            case "reply":
                art=new Article();
                bsuser=(Bbsuser) req.getSession().getAttribute("user");
                rootid=req.getParameter("rootid"); //点击回帖的帖子id
                art.setUser(bsuser); //当前回帖的用户
                art.setRootid(Integer.parseInt(rootid)); //不为0即从帖
                art.setContent(req.getParameter("content"));
                art.setTitle(req.getParameter("title"));
                art.setDatetime(new Date(System.currentTimeMillis()));
                service.save(art);
                dispatcher=req.getRequestDispatcher(map.get("showreply")+rootid);
                dispatcher.forward(req,resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        map.put("show",config.getInitParameter("show"));
        map.put("welcome",config.getInitParameter("welcome"));
        map.put("showreply",config.getInitParameter("showreply"));
    }
}
