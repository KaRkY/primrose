package primrose.spring;

import java.io.IOException;
import java.util.Random;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Profile("dev")
public class SlowFilter implements Filter {

  private Random random;

  @Override
  public void init(final FilterConfig filterConfig) throws ServletException {
    random = new Random();
  }

  @Override
  public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
    throws IOException, ServletException {
    final int delay = random.nextInt(2000) + 500;

    try {
      Thread.sleep(delay);
    } catch (final InterruptedException e) {
      throw new ServletException(e);
    }

    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {
  }

}
