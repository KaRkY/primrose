package primrose.rest.customer;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customers/{customer}")
public class CustomerController {

  @GetMapping
  public ResponseEntity<FullCustomer> read(@PathVariable("customer") CustomerCode customerCode) {
    System.out.println(customerCode);
    return ResponseEntity.ok().build();
  }

  @PostMapping
  public ResponseEntity<FullCustomer> update(@PathVariable("customer") CustomerCode customerCode, @RequestBody UpdateCustomer customer) {
    System.out.println(customer);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping
  public ResponseEntity<CustomerCode> delete(@PathVariable("customer") CustomerCode customerCode) {
    System.out.println(customerCode);
    return ResponseEntity.ok().build();
  }
}
