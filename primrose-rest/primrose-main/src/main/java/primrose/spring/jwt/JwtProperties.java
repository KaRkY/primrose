package primrose.spring.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "primrose.security")
public class JwtProperties {

  private final String secret = "c2VjcmV0"; // secret
  private final long expirationTime = 864000000; // 10 Days
  private final String tokenPrefix = "Bearer ";
  private final String header = "Authorization";
  private final String loginUrl = "/login";

  public String getSecret() {
    return secret;
  }

  public long getExpirationTime() {
    return expirationTime;
  }

  public String getTokenPrefix() {
    return tokenPrefix;
  }

  public String getHeader() {
    return header;
  }

  public String getLoginUrl() {
    return loginUrl;
  }

}
