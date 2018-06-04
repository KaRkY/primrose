package primrose.rest.customer;

import java.util.List;

import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.ImmutableSet;

import cz.jirutka.rsql.parser.ast.Node;
import primrose.jooq.FilterMapper;
import primrose.rest.PageRequest;
import primrose.rest.ResultPage;

@Controller
@RequestMapping("/customers")
public class CustomersController {
  private FilterMapper filterMapper;

  public CustomersController(FilterMapper filterMapper) {
    this.filterMapper = filterMapper;
  }

  @PostMapping
  public ResponseEntity<FullCustomer> create(@RequestBody CreateCustomer customer) {
    System.out.println(customer);
    return ResponseEntity.ok().build();
  }

  @GetMapping
  public ResponseEntity<ResultPage<PreviewCustomer>> read(PageRequest pagination, Node filter) {
    System.out.println(pagination);
    System.out.println(filterMapper.map(ImmutableSet.<Field<?>>builder()
      .add(DSL.field("fullName", String.class))
      .add(DSL.field("displayName", String.class))
      .add(DSL.field("age", Integer.class))
      .build(), filter));

    return ResponseEntity.ok().build();
  }

  @DeleteMapping
  public ResponseEntity<List<CustomerCode>> delete(Node filter) {
    return ResponseEntity.ok().build();
  }
}
