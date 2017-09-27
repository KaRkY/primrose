package primrose.spring;

import java.util.Arrays;
import java.util.Collection;

import org.aopalliance.intercept.MethodInvocation;
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
    if (object instanceof MethodInvocation) {
      final MethodInvocation inv = (MethodInvocation) object;
      System.out.println(String.format("method: %s", Arrays.toString(inv.getArguments())));
    }
    for (final ConfigAttribute attribute : attributes) {
      System.out.println(String.format("attribute: %s - %s", attribute, attribute.getClass()));
    }
    return ACCESS_GRANTED;
  }

}
