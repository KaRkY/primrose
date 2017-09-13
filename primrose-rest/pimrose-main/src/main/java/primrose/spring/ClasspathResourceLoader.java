package primrose.spring;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class ClasspathResourceLoader implements ResourceLoader {

  @Override
  public Resource getResource(final String location) {
    return new ClassPathResource(location);
  }

  @Override
  public ClassLoader getClassLoader() {
    return Thread.currentThread().getContextClassLoader();
  }

}
