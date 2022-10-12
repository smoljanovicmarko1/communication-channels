package com.remare.communicationchannelsauthorizationserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

  private final AuthenticationManager authenticationManager;

  @Autowired
  public AuthorizationServerConfig(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients
        .inMemory() // TODO read from database
        .withClient("client1")
        .secret("{noop}secret1")
        .authorizedGrantTypes("password")
        .scopes("read")
        .accessTokenValiditySeconds(1000000000)
        .and()
        .withClient("rs")
        .secret("{noop}rs");
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
    endpoints
        .authenticationManager(authenticationManager)
        .tokenStore(tokenStore())
        .accessTokenConverter(jwtAccessTokenConverter());
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) {
    security.tokenKeyAccess("isAuthenticated()");
  }

  @Bean
  public TokenStore tokenStore() {
    return new JwtTokenStore(jwtAccessTokenConverter());
  }

  @Bean
  public JwtAccessTokenConverter jwtAccessTokenConverter() {
    JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
    KeyStoreKeyFactory keyStoreKeyFactory =
        new KeyStoreKeyFactory(
            new ClassPathResource("ssia.jks"),
            "ssia123".toCharArray() // TODO don't store password like this
            );
    jwtAccessTokenConverter.setKeyPair(keyStoreKeyFactory.getKeyPair("ssia"));
    return jwtAccessTokenConverter;
  }
}
