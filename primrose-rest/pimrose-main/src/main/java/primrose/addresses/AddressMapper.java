package primrose.addresses;

import org.jooq.Record;

import pimrose.jooq.tables.TAddresses;

public class AddressMapper {

  public static Address mapAddress(final TAddresses addresses, final Record record) {
    final Address result = new Address();

    result.setId(record.getValue(addresses.ADDRESS_ID));
    result.setStreet(record.getValue(addresses.STREET));
    result.setState(record.getValue(addresses.STATE));
    result.setPostalCode(record.getValue(addresses.POSTAL_CODE));
    result.setCountry(record.getValue(addresses.COUNTRY));
    result.setCity(record.getValue(addresses.CITY));

    return result;
  }

}
