package primrose;

import java.io.IOException;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import primrose.config.RootConfiguration;
import primrose.config.SecurityConfiguration;
import primrose.config.WebConfiguration;

@SpringBootApplication
public class Main {

  public static void main(final String[] args) throws IOException {
    new SpringApplicationBuilder()
      .sources(
        Main.class,
        RootConfiguration.class,
        WebConfiguration.class,
        SecurityConfiguration.class)
      .run(args);
  }
}
