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

  String create(@JsonRpcParam("contact") ContactCreate contact);

  ContactFullDisplay get(@JsonRpcParam("contactCode") String contactCode);

  String delete(@JsonRpcParam("contactCode") String contactCode);

  Set<String> delete(@JsonRpcParam("contactCodes") Set<String> contactCodes);

  String edit(@JsonRpcParam("contactCode") String contactCode, @JsonRpcParam("contact") ContactCreate contact);
}
