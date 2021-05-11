package com.jzue.note.security.notesecurity.server;

import com.jzue.note.security.notesecurity.service.UserService;
import com.jzue.note.security.notesecurity.service.UserService1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author jzue
 * @date 2019/9/27 下午2:55
 **/
@Configuration
public class OAuthServerConfig {
    private static final String DEMO_RESOURCE_ID = "order";

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.resourceId(DEMO_RESOURCE_ID).stateless(true);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {

//            http
//                    .authorizeRequests()
//                    // api 相关必须认证以后才可以访问
//                    .antMatchers("/api/**").authenticated();

            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                         .and()
                        .requestMatchers().anyRequest()
                         .and()
                         .anonymous()
                         .and()
             //            .authorizeRequests()
             //            .antMatchers("/order/**").authenticated();//配置order访问控制，必须认证过后才可以访问
                         .authorizeRequests()
                         .antMatchers("/order/**").hasAuthority("admin_role");//配置访问控制，必须具有admin_role权限才可以访问资源
             //            .antMatchers("/order/**").hasAnyRole("admin");

        }
    }

    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

        @Autowired
        AuthenticationManager authenticationManager;
        @Autowired
        RedisConnectionFactory redisConnectionFactory;

        @Autowired
        private UserService1 userService1;

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//            String finalSecret = "{bcrypt}"+new BCryptPasswordEncoder().encode("123456");

//            clients.inMemory().withClient("client_1")
//                    .resourceIds(DEMO_RESOURCE_ID)
//                    .authorizedGrantTypes("client_credentials", "refresh_token")
//                    .scopes("select")
//                    .authorities("oauth2")
//                    .secret(finalSecret)
//                    .and().withClient("client_2")
//                    .resourceIds(DEMO_RESOURCE_ID)
//                    .authorizedGrantTypes("password", "refresh_token")
//                    .scopes("select")
//                    .authorities("oauth2")
//                    .secret(finalSecret);
            clients.withClientDetails(userService1 );
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints
                    .tokenStore(new RedisTokenStore(redisConnectionFactory))
                    .authenticationManager(authenticationManager)
                    .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);

            //配置TokenService参数
            DefaultTokenServices tokenService = new DefaultTokenServices();
            tokenService.setTokenStore(endpoints.getTokenStore());
            tokenService.setSupportRefreshToken(true);
            tokenService.setClientDetailsService(endpoints.getClientDetailsService());
            tokenService.setTokenEnhancer(endpoints.getTokenEnhancer());
            tokenService.setAccessTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30)); //30天
            // tokenService.setRefreshTokenValiditySeconds((int)TimeUnit.DAYS.toSeconds(50)); //50天
            tokenService.setReuseRefreshToken(false);
            endpoints.tokenServices(tokenService);
        }


        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
            //允许表单认证
            //这里增加拦截器到安全认证链中，实现自定义认证，包括图片验证，短信验证，微信小程序，第三方系统，CAS单点登录
            //addTokenEndpointAuthenticationFilter(IntegrationAuthenticationFilter())
            //IntegrationAuthenticationFilter 采用 @Component 注入
            oauthServer.allowFormAuthenticationForClients()
                    .tokenKeyAccess("isAuthenticated()")
                    .checkTokenAccess("permitAll()");
        }

    }
}
