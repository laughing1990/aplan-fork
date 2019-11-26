package com.augurit.aplanmis;

import com.augurit.aplanmis.rest.filter.MobileTokenFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)
                ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)
                        ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)
                                http.addFilterAfter(new MobileTokenFilter(), SecurityContextPersistenceFilter.class)
                                        .sessionManagement()
                                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                                        .and()
                                        .antMatcher("/rest/**")
                                        .authorizeRequests()
                                        .antMatchers(new String[]{
                                                "/rest/opus/om/listUserTopOrg.do"
                                                , "/rest/user/**"
                                                , "/rest/index/**"
                                                , "/rest/guide/**"
                                                , "/rest/html5/**"
                                                , "/rest/area/**"
                                                , "/rest/send/short/message/**"
                                                , "/province/**"
                                                , "/rest/mall/mobile/login"
                                                , "/rest/mobile/**"
                                        })
                        )
                                .permitAll()
                                .antMatchers(HttpMethod.GET, new String[]{"/rest/**"})
                )
                        .access("#oauth2.hasScope('all')")
                        .antMatchers(HttpMethod.POST, new String[]{"/rest/**"})
        ).access("#oauth2.hasScope('all')");
    }
}
