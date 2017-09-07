package primrose;

import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.jndi.JndiLocatorDelegate;

@SpringBootApplication
public class Main extends SpringBootServletInitializer {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final JndiLocatorDelegate jndi = JndiLocatorDelegate.createDefaultResourceRefLocator();

  @Override
  protected SpringApplicationBuilder configure(final SpringApplicationBuilder builder) {

    try {
      final String profile = jndi.lookup("spring.profile.active", String.class);
      builder.profiles(profile);
    } catch (final NamingException e) {
      // If lookup is missing do nothing
      logger.info("Ignoring missing jndi profile name.");
    }

    return builder.sources(Main.class);
  }

  public static void main(final String[] args) {
    SpringApplication.run(Main.class, args);
  }

}
