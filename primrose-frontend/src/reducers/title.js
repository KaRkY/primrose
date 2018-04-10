export default (state = "Dashboard", action = {}) => {
  switch (action.type) {
    case "DASHBOARD":
      return "Dashboard";
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
    case "CONTACTS_NEW":
      return "New Contact";
    default:
      return state;
  }
};