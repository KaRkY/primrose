package primrose.data;

import java.util.List;

import primrose.data.impl.MetaRepositoryImpl.MetaTypes;
import primrose.service.MetaType;

public interface MetaRepository {

   List<MetaType> list(MetaTypes type);

   boolean contains(MetaTypes type, String value);

}
