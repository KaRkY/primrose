import { schema } from "normalizr";

const contact = new schema.Entity("contact");
const contacts = new schema.Array(contact);
const address = new schema.Entity("address");
const addresses = new schema.Array(address);
const account = new schema.Entity(
  "account", {
    addresses,
    contacts,
  });
const accounts = new schema.Array(account);


export default {
  account,
  accounts,
};
