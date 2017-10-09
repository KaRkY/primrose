package primrose.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;

public class DefaultRolesPrefixPostProcessor implements BeanPostProcessor, PriorityOrdered {

  @Override
  public Object postProcessAfterInitialization(final Object bean, final String beanName)
    throws BeansException {

    if (bean instanceof DefaultMethodSecurityExpressionHandler) {
      ((DefaultMethodSecurityExpressionHandler) bean).setDefaultRolePrefix(null);
    }
    if (bean instanceof DefaultWebSecurityExpressionHandler) {
      ((DefaultWebSecurityExpressionHandler) bean).setDefaultRolePrefix(null);
    }
    if (bean instanceof SecurityContextHolderAwareRequestFilter) {
      ((SecurityContextHolderAwareRequestFilter) bean).setRolePrefix("");
    }

    if (bean instanceof RoleVoter) {
      ((RoleVoter) bean).setRolePrefix("");
      System.out.println("Dela");
    }
    return bean;
  }

  @Override
  public Object postProcessBeforeInitialization(final Object bean, final String beanName)
    throws BeansException {
    return bean;
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE;
  }
}
