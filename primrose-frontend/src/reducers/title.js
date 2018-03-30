export default (state = "Home", action = {}) => {
  switch (action.type) {
    case "HOME":
      return "Home";
    case "CUSTOMERS":
      return "Customers";
    case "CUSTOMER":
      return "Customer";
    case "CUSTOMER_NEW":
      return "New Customer";
    case "CUSTOMER_EDIT":
      return "Edit Customer";
    default:
      return state;
  }
};