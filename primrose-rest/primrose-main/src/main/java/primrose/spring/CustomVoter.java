package primrose.spring;

import java.util.Collection;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

public class CustomVoter implements AccessDecisionVoter<Object> {

  @Override
  public boolean supports(final ConfigAttribute attribute) {
    System.out.println(String.format("supports: %s", attribute));
    return true;
  }

  @Override
  public boolean supports(final Class<?> clazz) {
    System.out.println(String.format("supports clazz: %s", clazz));
    return true;
  }

  @Override
  public int vote(final Authentication authentication, final Object object,
    final Collection<ConfigAttribute> attributes) {
    System.out.println(attributes);
    return ACCESS_GRANTED;
  }

}
