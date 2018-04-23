package primrose.rpcservices;

import java.util.Set;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;

import primrose.service.ListResult;
import primrose.service.Pagination;
import primrose.service.contact.ContactCreate;
import primrose.service.contact.ContactEdit;
import primrose.service.contact.ContactFullDisplay;
import primrose.service.contact.ContactReducedDisplay;

@JsonRpcService("/contacts")
public interface ContactsRpc {

  String create(@JsonRpcParam("contact") ContactCreate contact);

  String update(@JsonRpcParam("contactCode") String contactCode, @JsonRpcParam("contact") ContactEdit contact);

  ListResult<ContactReducedDisplay> list(@JsonRpcParam("pagination") Pagination pagination);

  ContactFullDisplay get(@JsonRpcParam("contactCode") String contactCode);

  String delete(@JsonRpcParam("contactCode") String contactCode);

  Set<String> delete(@JsonRpcParam("contactCodes") Set<String> contactCodes);
}
