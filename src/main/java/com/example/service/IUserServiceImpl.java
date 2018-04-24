package com.example.service;

import com.example.dao.IUserDao;
import com.example.po.Bbsuser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by samsung on 2017/10/28.
 */
@Service
public class IUserServiceImpl {
    private Map<String,String> types=new HashMap<String,String>();
    public IUserServiceImpl(){
        types.put("image/jpeg", ".jpg");
        types.put("image/gif", ".gif");
        types.put("image/x-ms-bmp", ".bmp");
        types.put("image/png", ".png");
    }
    @Autowired
    private IUserDao dao;

    public Bbsuser upload(HttpServletRequest req){
        //让该组件作用到全部的Appliaction中
        CommonsMultipartResolver commonsMultipartFile=
                new CommonsMultipartResolver(req.getSession().getServletContext());
        //文件名字中可以有中文
        commonsMultipartFile.setDefaultEncoding("utf-8");
        //延时加载 防止上传大文件而提前解析
        commonsMultipartFile.setResolveLazily(true);
        //上传文件的最大总和
        commonsMultipartFile.setMaxUploadSize(1024*1024*5);
        //每个最大上传文件
        commonsMultipartFile.setMaxUploadSizePerFile(1024*1024);
        //设置缓存
        commonsMultipartFile.setMaxInMemorySize(1024*1024*4);
        MultipartHttpServletRequest mrequest
                =commonsMultipartFile.resolveMultipart(req);
        //取得文件域
        MultipartFile mfile =mrequest.getFile("file0");
        String filename=mfile.getOriginalFilename();
      //  String type=types.get(file.getContentType());
        String filepath="upload"+File.separator+filename;
        File file=new File(filepath);
        try {
            mfile.transferTo(file);//将上传的文件写到指定服务器位置
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bbsuser bbsuser=new Bbsuser();
        bbsuser.setUsername(mrequest.getParameter("reusername"));//获得注册的姓名、密码
        bbsuser.setPassword(mrequest.getParameter("repassword"));
        bbsuser.setPicPath(filepath);
        try (InputStream fis=new FileInputStream(file)){

            byte[] buffer=new byte[fis.available()];
            fis.read(buffer);
            bbsuser.setPic(buffer);//把文件以二进制流的形式存起来
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bbsuser;
    }
    public Bbsuser login(Bbsuser bbsuser){

        return dao.login(bbsuser.getUsername(),bbsuser.getPassword());
    }
    public Bbsuser save(Bbsuser bbsuser){
        return dao.save(bbsuser);
    }

    public Bbsuser getPic(@Param("id") String id){
        return dao.getPic(Integer.parseInt(id));
    }
}
