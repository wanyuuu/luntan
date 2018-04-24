package com.example;

import com.example.filter.EncodingFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;

@SpringBootApplication
@ServletComponentScan //servlet扫描
public class Application {//springboot入口
	//自定义Filter
	@Bean
	public FilterRegistrationBean encodingFilterRegistration() {

		FilterRegistrationBean filterRegistrationBean= new FilterRegistrationBean();
		filterRegistrationBean.setFilter(encodingFilter());
		filterRegistrationBean.setName("myEn");
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.addInitParameter("encoder","UTF-8");
		filterRegistrationBean.setOrder(1);
		return filterRegistrationBean;
	}
	@Bean(name="myEn")
	public Filter encodingFilter(){
		return new EncodingFilter();
	}
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
