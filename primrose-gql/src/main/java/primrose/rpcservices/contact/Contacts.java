package primrose.rpcservices.contact;

import java.util.Set;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;

import primrose.service.Search;
import primrose.service.SearchResult;
import primrose.service.contact.ContactCreate;
import primrose.service.contact.ContactSearch;

@JsonRpcService("/contacts")
public interface Contacts {

  SearchResult<ContactSearch> search(@JsonRpcParam("search") Search search);

  long create(@JsonRpcParam("contact") ContactCreate contact);

  long delete(@JsonRpcParam("contact") long id);

  Set<Long> delete(@JsonRpcParam("contacts") Set<Long> ids);
}
