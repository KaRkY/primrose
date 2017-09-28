package primrose.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

  @Override
  protected AccessDecisionManager accessDecisionManager() {
    final List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<>();
    final RoleVoter roleVoter = new RoleVoter();
    roleVoter.setRolePrefix("");
    decisionVoters.add(roleVoter);
    decisionVoters.add(new AuthenticatedVoter());
    return new AffirmativeBased(decisionVoters);
  }
}
