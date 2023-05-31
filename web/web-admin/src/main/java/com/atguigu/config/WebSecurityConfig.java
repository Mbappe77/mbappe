package com.atguigu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启权限注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
/*    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("eason")
                .password(new BCryptPasswordEncoder().encode("123456"))
                .roles("");
    }*/

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().sameOrigin();
        //配置可以匿名访问的资源及其他所有资源需要认证之后才能访问
        http.authorizeRequests().antMatchers("/static/**","/login").permitAll().anyRequest().authenticated();
        //配置登录的地址及登录成功之后去往的地址
        http.formLogin().loginPage("/login").defaultSuccessUrl("/");
        //配置登出的路径及登出成功去往的地址
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/login");
        //关闭跨域请求伪造功能
        http.csrf().disable();
        //异常入口
        http.exceptionHandling().accessDeniedHandler(new CustomAccessDeineHandler());
    }
}
