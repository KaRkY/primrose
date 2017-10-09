package primrose.spring.jwt;

import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

  private final JwtProperties jwtProperties;

  public JwtAuthorizationFilter(final AuthenticationManager authManager, final JwtProperties jwtProperties) {
    super(authManager);
    this.jwtProperties = jwtProperties;
  }

  @Override
  protected void doFilterInternal(final HttpServletRequest req,
    final HttpServletResponse res,
    final FilterChain chain) throws IOException, ServletException {
    final String header = req.getHeader(jwtProperties.getHeader());

    if (header == null || !header.startsWith(jwtProperties.getTokenPrefix())) {
      chain.doFilter(req, res);
      return;
    }

    final UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(req, res);
  }

  @SuppressWarnings("unchecked")
  private UsernamePasswordAuthenticationToken getAuthentication(final HttpServletRequest request) {
    final String token = request.getHeader(jwtProperties.getHeader());
    if (token != null) {
      // parse the token.
      final Claims body = Jwts.parser()
        .setSigningKey(jwtProperties.getSecret())
        .parseClaimsJws(token.replace(jwtProperties.getTokenPrefix(), ""))
        .getBody();

      if (body.getSubject() != null) {

        return new UsernamePasswordAuthenticationToken(
          body.getSubject(),
          null,
          ((List<Object>) body.get("permissons"))
            .stream()
            .map(Object::toString)
            .map(SimpleGrantedAuthority::new)
            .collect(toSet()));
      }
      return null;
    }
    return null;
  }
}
