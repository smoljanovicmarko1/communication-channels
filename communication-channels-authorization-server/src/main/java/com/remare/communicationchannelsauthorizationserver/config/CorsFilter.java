package com.remare.communicationchannelsauthorizationserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.remare.communicationchannelsauthorizationserver.constant.CorsConstants.*;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@WebFilter("/*")
public class CorsFilter implements Filter {

  @Value("${endpoints.cors.allowed-origins}")
  private String allowedOrigins;

  @Value("${endpoints.cors.allowed-methods}")
  private String allowedMethods;

  @Value("${endpoints.cors.allow-credentials}")
  private String allowCredentials;

  @Value("${endpoints.cors.allowed-headers}")
  private String allowedHeaders;

  @Value("${endpoints.cors.max-age}")
  private String maxAge;

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    final HttpServletResponse response = (HttpServletResponse) res;
    response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, allowedOrigins);
    response.setHeader(ACCESS_CONTROL_ALLOW_METHODS, allowedMethods);
    response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS, allowedHeaders);
    response.setHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, allowCredentials);
    response.setHeader(ACCESS_CONTROL_MAX_AGE, maxAge);
    if (OPTIONS.equalsIgnoreCase(((HttpServletRequest) req).getMethod())) {
      response.setStatus(HttpServletResponse.SC_OK);
    } else {
      chain.doFilter(req, res);
    }
  }
}
