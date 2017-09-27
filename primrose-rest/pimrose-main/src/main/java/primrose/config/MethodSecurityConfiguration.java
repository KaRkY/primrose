package primrose.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import primrose.spring.CustomVoter;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {
@Override
protected MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
  return super.customMethodSecurityMetadataSource();
}
  @Override
  protected AccessDecisionManager accessDecisionManager() {
    final List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<>();
    decisionVoters.add(new CustomVoter());
    decisionVoters.add(new RoleVoter());
    decisionVoters.add(new AuthenticatedVoter());
    return new AffirmativeBased(decisionVoters);
  }
}
