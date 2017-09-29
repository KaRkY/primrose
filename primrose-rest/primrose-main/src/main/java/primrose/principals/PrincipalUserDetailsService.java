package primrose.principals;

import static java.util.stream.Collectors.toList;

import org.jooq.exception.NoDataFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PrincipalUserDetailsService implements UserDetailsService {

  private final PrincipalsRepository principalsRepository;

  public PrincipalUserDetailsService(final PrincipalsRepository principalsRepository) {
    this.principalsRepository = principalsRepository;
  }

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    try {
      final Principal principal = principalsRepository.loadPrincipal(username);

      return new User(
        principal.name(),
        principal.credidentials(),
        principal.enabled(),
        true,
        true,
        !principal.locked(),
        principal.permissions().stream()
          .map(SimpleGrantedAuthority::new)
          .collect(toList()));
    } catch (final NoDataFoundException e) {
      throw new UsernameNotFoundException("Principal not found.", e);
    }
  }
}
