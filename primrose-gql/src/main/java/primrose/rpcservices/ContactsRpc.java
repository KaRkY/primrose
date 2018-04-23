package primrose.rpcservices;

import java.util.Set;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;

import primrose.service.ListResult;
import primrose.service.Pagination;
import primrose.service.contact.ContactCreate;
import primrose.service.contact.ContactFullDisplay;
import primrose.service.contact.ContactReducedDisplay;

@JsonRpcService("/contacts")
public interface ContactsRpc {

  ListResult<ContactReducedDisplay> list(@JsonRpcParam("pagination") Pagination pagination);

  String create(@JsonRpcParam("contact") ContactCreate contact);

  ContactFullDisplay get(@JsonRpcParam("contactCode") String contactCode);

  String deactivate(@JsonRpcParam("contactCode") String contactCode);

  Set<String> deactivate(@JsonRpcParam("contactCodes") Set<String> contactCodes);

  String edit(@JsonRpcParam("contactCode") String contactCode, @JsonRpcParam("contact") ContactCreate contact);
}
