package primrose.rpcservices;

import java.util.Set;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;

@JsonRpcService("/contacts")
public interface Contacts {

  ContactsSearchResult search(@JsonRpcParam("search") Search search);

  long create(@JsonRpcParam("contact") ContactCreate contact);

  long delete(@JsonRpcParam("contact") long id);

  Set<Long> delete(@JsonRpcParam("contacts") Set<Long> ids);
}
