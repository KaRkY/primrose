package primrose.spring;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class FileSQLLoader implements SQLLoader {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final ResourceLoader resourceLoader;

  public FileSQLLoader(final ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  @Override
  public String loadSQL(final String name) {
    logger.debug("Loading sql: {}", name);
    final Resource sqlFile = resourceLoader.getResource(String.format("%s.sql", name.replaceAll("\\.", "/")));
    try(InputStream in = sqlFile.getInputStream();
      Reader rin = new InputStreamReader(in, StandardCharsets.UTF_8);
      BufferedReader brin = new BufferedReader(rin)){

      return brin.lines().collect(Collectors.joining("\n"));
    } catch (final IOException e) {
      throw new RuntimeException("Could not read file.", e);
    }
  }

}
