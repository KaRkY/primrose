import { schema } from "normalizr";

const address = new schema.Entity("address");
const addresses = new schema.Array(address);
const addressTypes = new schema.Values(addresses);
const account = new schema.Entity(
  "account", {
    addresses: addressTypes
  }, {
    idAttribute: "code"
  });
const accounts = new schema.Array(account);

export default {
  account,
  accounts
};