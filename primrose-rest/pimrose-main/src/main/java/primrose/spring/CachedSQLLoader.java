package primrose.spring;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class CachedSQLLoader implements SQLLoader {
  private final Cache<String, String> sqlCache;
  private final SQLLoader loader;

  public CachedSQLLoader(final SQLLoader loader) {
    this.loader = loader;
    sqlCache = CacheBuilder
      .newBuilder()
      .expireAfterAccess(10, TimeUnit.MINUTES)
      .build();
  }

  @Override
  public String loadSQL(final String name) {
    try {
      return sqlCache.get(name, () -> loader.loadSQL(name));
    } catch (final ExecutionException e) {
      throw new RuntimeException("Could not load sql.", e);
    }
  }
}
