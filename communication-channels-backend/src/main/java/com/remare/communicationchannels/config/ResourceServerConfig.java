package com.remare.communicationchannels.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .mvcMatchers(HttpMethod.DELETE, "/channel")
        .hasAuthority("channel:delete")
        .mvcMatchers("/channel/save")
        .hasAuthority("channel:write")
        .mvcMatchers("/user/add")
        .hasAuthority("user:write")
        .mvcMatchers("/user/verify-activation-token")
        .permitAll()
        .mvcMatchers("/user/make-account")
        .permitAll();
  }
}
