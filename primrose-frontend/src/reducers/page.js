import { NOT_FOUND } from "redux-first-router";

export default (state = "PageHome", action = {}) => components[action.type] || state;

const components = {
  DASHBOARD: "PageDashboard",
  ERROR: "ER",
  CUSTOMERS: "PageCustomers",
  CUSTOMER: "PageCustomer",
  CUSTOMER_NEW: "PageNewCustomer",
  CUSTOMER_EDIT: "PageEditCustomer",
  CONTACTS: "PageContacts",
  CONTACT: "PageContact",
  CONTACT_NEW: "PageNewContact",
  CONTACT_EDIT: "PageEditContact",
  [NOT_FOUND]: "PageError"
}