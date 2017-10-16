package primrose.accounts;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import primrose.addresses.AddressResourceAssembler;
import primrose.addresses.AddressesService;

@Controller
@RequestMapping(path = "/accounts/{account}/addresses")
public class AccountAddressesController {

  private final AccountsService          accountsService;
  private final AddressesService         addressesService;
  private final AddressResourceAssembler addressResourceAssembler;

  public AccountAddressesController(
    final AccountsService accountsService,
    final AddressesService addressesService,
    final AddressResourceAssembler addressResourceAssembler) {
    this.accountsService = accountsService;
    this.addressesService = addressesService;
    this.addressResourceAssembler = addressResourceAssembler;
  }

}
