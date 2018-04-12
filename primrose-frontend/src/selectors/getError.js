import getPage from "./getPage";
import getCustomersError from "./customers/getError";
import getContactsError from "./contacts/getError";

export default state => {
  switch(getPage(state)) {
    case "PageCustomers": return getCustomersError(state);
    case "PageContacts": return getContactsError(state);
    default: return null;
  }
};