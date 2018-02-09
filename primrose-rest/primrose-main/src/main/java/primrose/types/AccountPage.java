package primrose.types;

import java.util.List;

import org.immutables.value.Value;

@Value.Immutable
public interface AccountPage {
  Integer pageNumber();

  Integer pageSize();

  Integer totalSize();

  List<Account> data();
}
