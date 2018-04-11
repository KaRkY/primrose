import getPage from "./getPage";
import isCustomersLoading from "./customers/isLoading";
import isContactsLoading from "./contacts/isLoading";

export default state => {
  switch(getPage(state)) {
    case "PageCustomers": return isCustomersLoading(state);
    case "PageContacts": return isContactsLoading(state);
    default: return false;
  }
};