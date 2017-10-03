package primrose.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import primrose.principals.PrincipalUserDetailsService;
import primrose.spring.DefaultRolesPrefixPostProcessor;
import primrose.spring.jwt.JwtAuthenticationFilter;
import primrose.spring.jwt.JwtAuthorizationFilter;
import primrose.spring.jwt.JwtProperties;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
  private final PrincipalUserDetailsService principalUserDetailsService;
  private final JwtProperties jwtProperties;

  public SecurityConfiguration(
    final PrincipalUserDetailsService principalUserDetailsService,
    final JwtProperties jwtProperties) {
    this.principalUserDetailsService = principalUserDetailsService;
    this.jwtProperties = jwtProperties;
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public static DefaultRolesPrefixPostProcessor defaultRolesPrefixPostProcessor() {
    return new DefaultRolesPrefixPostProcessor();
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
    final JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtProperties);
    filter.setAuthenticationManager(authenticationManager());
    return filter;
  }

  @Bean
  public JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
    return new JwtAuthorizationFilter(authenticationManager(), jwtProperties);
  }

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http
      .cors()
      .and()
      .csrf()
      .disable()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      .authorizeRequests()
      .antMatchers(jwtProperties.getLoginUrl()).permitAll()
      .antMatchers("/error").permitAll()
      .anyRequest().authenticated()
      .and()
      .addFilterAt(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
      .addFilterAfter(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
  }

  @Override
  protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
    auth
      .userDetailsService(principalUserDetailsService)
      .passwordEncoder(bCryptPasswordEncoder());
  }
}
