package primrose.spring.jwt;

import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * Filter that orchestrates authentication by using supplied JWT token
 *
 * @author pascal alma
 */
public class JwtAuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {

  private final JwtProperties jwtProperties;

  public JwtAuthenticationTokenFilter(final JwtProperties jwtProperties) {
    super("/**");
    this.jwtProperties = jwtProperties;
  }

  /**
   * Attempt to authenticate request - basically just pass over to another method to authenticate request headers
   */
  @SuppressWarnings("unchecked")
  @Override
  public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) {
    final String header = request.getHeader(jwtProperties.getHeader());

    if (header == null || !header.startsWith(jwtProperties.getTokenPrefix())) {
      return null;
    }

    final String authToken = header.replace(jwtProperties.getTokenPrefix(), "");

    final Claims body = Jwts.parser()
      .setSigningKey(jwtProperties.getSecret())
      .parseClaimsJws(authToken)
      .getBody();

    return new UsernamePasswordAuthenticationToken(body.getSubject(),
                                                   null,
                                                   ((List<Object>) body.get("permissons"))
                                                     .stream()
                                                     .map(Object::toString)
                                                     .map(SimpleGrantedAuthority::new)
                                                     .collect(toSet()));
  }

  /**
   * Make sure the rest of the filterchain is satisfied
   *
   * @param request
   * @param response
   * @param chain
   * @param authResult
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,
    final FilterChain chain, final Authentication authResult)
    throws IOException, ServletException {
    super.successfulAuthentication(request, response, chain, authResult);

    // As this authentication is in HTTP header, after success we need to
    // continue the request normally
    // and return the response as if the resource was not secured at all
    chain.doFilter(request, response);
  }
}
