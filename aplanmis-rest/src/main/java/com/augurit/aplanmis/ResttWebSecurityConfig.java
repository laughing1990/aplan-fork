package com.augurit.aplanmis;

import com.augurit.agcloud.framework.config.FrameworkSsoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableOAuth2Sso
@Configuration
@ComponentScan(basePackages = {"com.augurit.agcloud.framework.config"})
public class ResttWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private FrameworkSsoProperties frameworkSsoProperties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        http.csrf().disable();
        if (frameworkSsoProperties.getEnable()) {
            http.logout()
                    .logoutSuccessUrl(frameworkSsoProperties.getSsoLogoutUrl())
                    .deleteCookies("JSESSIONID");
        } else {
            //全部资源不需要认证
            http.authorizeRequests()
                    .antMatchers("/**").permitAll();
        }
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/docs/**", "/imgs/**", "/webjars/**");
    }
}
