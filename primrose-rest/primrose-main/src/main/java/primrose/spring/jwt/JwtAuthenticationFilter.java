package primrose.spring.jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import primrose.principals.ImmutableLoginUser;
import primrose.principals.LoginUser;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final JwtProperties jwtProperties;

  public JwtAuthenticationFilter(final JwtProperties jwtProperties) {
    super();
    this.jwtProperties = jwtProperties;
    setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(jwtProperties.getLoginUrl()));
  }

  @Override
  public Authentication attemptAuthentication(final HttpServletRequest req,
    final HttpServletResponse res) throws AuthenticationException {
    try {
      final LoginUser creds = new ObjectMapper().readValue(req.getInputStream(), ImmutableLoginUser.class);

      return getAuthenticationManager().authenticate(
        new UsernamePasswordAuthenticationToken(
          creds.username(),
          creds.password(),
          new ArrayList<>()));
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(final HttpServletRequest req,
    final HttpServletResponse res,
    final FilterChain chain,
    final Authentication auth) throws IOException, ServletException {
    final String token = Jwts.builder()
      .setSubject(auth.getName())
      .claim("permissons",
        auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()))
      .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpirationTime()))
      .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())
      .compact();
    res.addHeader(jwtProperties.getHeader(), jwtProperties.getTokenPrefix() + token);
  }
}