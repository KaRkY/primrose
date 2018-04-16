package primrose.rpcservices.impl;

import java.util.ArrayList;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;

import primrose.rpcservices.contact.Contacts;
import primrose.service.Search;
import primrose.service.SearchResult;
import primrose.service.contact.ContactCreate;
import primrose.service.contact.ContactSearch;

@AutoJsonRpcServiceImpl
@Component
public class ContactsImpl implements Contacts {

  @Override
  public SearchResult<ContactSearch> search(Search search) {
    System.out.println(search);
    return new SearchResult<>(new ArrayList<>(), 0);
  }

  @Override
  public long create(ContactCreate contact) {
    System.out.println(contact);
    return 0;
  }

  @Override
  public long delete(long id) {
    System.out.println(id);
    return 0;
  }

  @Override
  public Set<Long> delete(Set<Long> ids) {
    System.out.println(ids);
    return null;
  }

}
