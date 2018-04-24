package com.example.controller;

import com.example.config.FreemarkerUtils;
import com.example.po.Bbsuser;
import com.example.service.IUserServiceImpl;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by samsung on 2017/10/28.
 */
@WebServlet(name = "userController",urlPatterns = {"/user"},
        initParams = {@WebInitParam(name="welcome",value = "welcome")})
public class UserController extends HttpServlet {
    @Autowired
    private IUserServiceImpl service;
    private Map<String,String> map=new HashMap<String,String>();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String,Object> vmap=new HashMap<String,Object>();
        if(ServletFileUpload.isMultipartContent(req)){//流形式

            Bbsuser user=service.upload(req);//将文件上传到服务器
            user=service.save(user);//存入数据库
            if(user!=null){
                vmap.put("msg","恭喜你，注册成功");
            }
            req.setAttribute("vmp",vmap); //在queryall处接收
            RequestDispatcher dispatcher1=req.getRequestDispatcher(map.get("welcome"));
            dispatcher1.forward(req,resp);
        }else {
            String action=req.getParameter("action");
            Bbsuser bbsuser=null;
            switch (action){
                case "login":
                    bbsuser=login(req,resp);
//                    vmap.put("msg","欢迎"+bbsuser.getUsername());
//                    vmap.put("user",bbsuser);
                    req.getSession().setAttribute("user",bbsuser);
                    // FreemarkerUtils.forward(resp,map.get("show"),vmap);
                    RequestDispatcher dispatcher=req.getRequestDispatcher(map.get("welcome"));
                    dispatcher.forward(req,resp);
                    break;
                case "out":
                    out(req,resp);
                    dispatcher=req.getRequestDispatcher(map.get("welcome"));
                    dispatcher.forward(req,resp);
                    //FreemarkerUtils.forward(resp,map.get("show"),vmap);
                    break;
                case "pic":
                    pic(req,resp);
                    //不能在响应结束后再向客户端（缓冲区）输出任何内容
                    break;
            }
        }
        //  FreemarkerUtils.forward(resp,map.get("show"),vmap);
    }

    private void pic(HttpServletRequest req,HttpServletResponse resp) {
        String id=req.getParameter("id");
        Bbsuser user=service.getPic(id);//根据id从数据库取得user
        try {
            OutputStream out=resp.getOutputStream();
            out.write(user.getPic());//将缓冲区的输入 输出到页面
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void out(HttpServletRequest request,HttpServletResponse resp) {
        request.getSession().invalidate();
        //FreemarkerUtils.forward(resp,map.get("show"),null);
    }


    private Bbsuser login(HttpServletRequest request,HttpServletResponse response) {
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        Bbsuser buser=new Bbsuser();
        buser.setUsername(username);
        buser.setPassword(password);
        buser=service.login(buser);
        if(buser!=null){ //登陆成功
            String sun=request.getParameter("sun");
            if(sun!=null){
                Cookie uc=new Cookie("papaoku",username);
                uc.setMaxAge(3600*24*7);
                response.addCookie(uc);
                Cookie pc=new Cookie("papaokp",password);
                pc.setMaxAge(3600*24*7);
                response.addCookie(pc);
            }
            return buser;
        }
        return null;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        map.put("welcome",config.getInitParameter("welcome"));
    }
}
