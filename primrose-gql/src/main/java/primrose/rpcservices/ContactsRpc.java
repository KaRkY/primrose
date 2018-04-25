package primrose.rpcservices;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;

import primrose.service.ListResult;
import primrose.service.Pagination;
import primrose.service.contact.Contact;
import primrose.service.contact.ContactCode;
import primrose.service.contact.ContactPreview;

@JsonRpcService("/contacts")
public interface ContactsRpc {

  ContactCode create(@JsonRpcParam("contact") Contact contact);

  ContactCode update(@JsonRpcParam("contact") Contact contact);

  ListResult<ContactPreview> list(@JsonRpcParam("pagination") Pagination pagination);

  Contact get(@JsonRpcParam("contactCode") ContactCode contactCode);
}
