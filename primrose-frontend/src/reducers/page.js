import { NOT_FOUND } from "redux-first-router";

export default (state = "PageHome", action = {}) => components[action.type] || state;

const components = {
  DASHBOARD: "PageDashboard",
  CUSTOMERS: "PageCustomers",
  CUSTOMER: "PageCustomer",
  CUSTOMER_NEW: "PageNewCustomer",
  CUSTOMER_EDIT: "PageEditCustomer",
  CONTACTS: "PageContacts",
  CONTACTS_NEW: "PageNewContacts",
  [NOT_FOUND]: "PageNotFound"
}