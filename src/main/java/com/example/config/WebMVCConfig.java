package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by samsung on 2017/10/28.
 */
@Configuration
public class WebMVCConfig extends WebMvcConfigurerAdapter {

    @Override//设置首页
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/welcome");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);//设置最高优先级
        super.addViewControllers(registry);
    }

    @Override//对静态资源进行处理 否则boot把所有静态进行拦截
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").
                addResourceLocations("classpath:/static/").resourceChain(true);
    }
}
