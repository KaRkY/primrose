package primrose.data;

import java.util.List;

import primrose.model.output.BaseOutputPrincipal;

public interface PrincipalsRepository {

  List<String> loadOperations();

  List<String> loadPermissions(String username);

  BaseOutputPrincipal loadPrincipal(String username);

  List<String> loadResources();

}
