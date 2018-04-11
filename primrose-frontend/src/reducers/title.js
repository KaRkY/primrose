import {
  NOT_FOUND
} from "redux-first-router";

export default (state = "Dashboard", action = {}) => {
  switch (action.type) {
    case "DASHBOARD":
      return "Dashboard";
    case "ERROR":
      return "Test error page";
    case "CUSTOMERS":
      return "Customers";
    case "CUSTOMER":
      return "Customer";
    case "CUSTOMER_NEW":
      return "New Customer";
    case "CUSTOMER_EDIT":
      return "Edit Customer";
    case "CONTACTS":
      return "Contacts";
    case "CONTACT":
      return "Contact";
    case "CONTACT_NEW":
      return "New Contact";
    case "CONTACT_EDIT":
      return "Edit Contact";
    case NOT_FOUND:
      return "Page not found";
    default:
      return state;
  }
};