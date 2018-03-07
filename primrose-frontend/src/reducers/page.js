import { NOT_FOUND } from "redux-first-router";

export default (state = "PageHome", action = {}) => components[action.type] || state;

const components = {
  HOME: "PageHome",
  CUSTOMERS: "PageCustomers",
  CUSTOMER: "PageCustomer",
  CUSTOMER_NEW: "PageNewCustomer",
  CUSTOMER_EDIT: "PageEditCustomer",
  [NOT_FOUND]: "PageNotFound"
}