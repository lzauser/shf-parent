package com.atguigu.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();//指定解码器,会自动解码
    }
    
    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("root")
                .password(new BCryptPasswordEncoder().encode("root"))//{noop}是不加密
                .roles("");
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //必须调用父类的方法，否则就不需要认证即可访问,但是这里使用了自定义login.html, 必须需要注释
        //super.configure(http);
        //允许iframe嵌套显示
        http.headers().frameOptions().disable();
        //1.设置资源放行
        http.authorizeRequests()
            //放行静态资源, login.html
            .antMatchers("/static/**", "/login").permitAll()
            //其他资源登录后即可访问
            .anyRequest().authenticated();
        //2.设置登录
        http.formLogin()
            //login.html页面访问路径
            .loginPage("/login")
            //登录功能请求路径
            .loginProcessingUrl("/login_process")
            //登录参数名称
            .usernameParameter("username")
            //密码参数名称
            .passwordParameter("password")
            //登录成功跳转路径
            .defaultSuccessUrl("/")
            //登录失败转发路径
            .failureUrl("/login");
        //3. 设置注销
        http.logout()
            //注销功能请求路径
            .logoutUrl("/logout")
            //注销成功跳转路径
            .logoutSuccessUrl("/login");
        //4. 禁用csrf
        http.csrf().disable();
        //5.设置自定义权限不足页面
        http.exceptionHandling()
                .accessDeniedHandler(new AtguiguAccessDeniedHandler());
    }
}
