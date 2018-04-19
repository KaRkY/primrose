package primrose.rpcservices;

import java.util.Set;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;

import primrose.service.Search;
import primrose.service.SearchResult;
import primrose.service.contact.ContactCreate;
import primrose.service.contact.ContactFullDisplay;
import primrose.service.contact.ContactReducedDisplay;

@JsonRpcService("/contacts")
public interface ContactsRpc {

  SearchResult<ContactReducedDisplay> search(@JsonRpcParam("search") Search search);

  long create(@JsonRpcParam("contact") ContactCreate contact);

  ContactFullDisplay get(@JsonRpcParam("contactId") long contactId);

  long delete(@JsonRpcParam("contactId") long contactId);

  Set<Long> delete(@JsonRpcParam("contactIds") Set<Long> contactIds);

  long edit(@JsonRpcParam("contactId") long contactId, @JsonRpcParam("contact") ContactCreate contact);
}
