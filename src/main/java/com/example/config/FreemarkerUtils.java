package com.example.config;


import freemarker.template.Configuration;
import freemarker.template.Template;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by samsung on 2017/10/28.
 */
public class FreemarkerUtils {
    private static Configuration conf;
    public static Configuration buildConfiguration(){
        if(conf==null){
            conf=new Configuration(Configuration.VERSION_2_3_26);
            //从Classpath根下目录取
            String path=FreemarkerUtils.class.getResource("/").getPath();
            File file =new File(path+File.separator+"templates");
            try {
                //加载模板
                conf.setDirectoryForTemplateLoading(file);
                conf.setDefaultEncoding("UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return conf;
    }

    /**
     *
     * @param resp  响应对象
     * @param targetName  目标页名称
     * @param vamp   键值
     */
    public static void forward(HttpServletResponse resp,
                               String targetName,
                               Map<String,Object> vamp){
        try {
            Template tmp=buildConfiguration().getTemplate(targetName);
            resp.setContentType("text/html");
            resp.setCharacterEncoding("utf-8");
            PrintWriter out=resp.getWriter();
            tmp.process(vamp,out); //将模板文件输出到浏览器上

            out.flush();
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
