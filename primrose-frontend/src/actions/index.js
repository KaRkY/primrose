import { NOT_FOUND } from "redux-first-router";

// try dispatching these from the redux devTools

export const goToPage = (type, category) => ({
  type,
  payload: category && { category }
});

export const goToDashboard = () => ({
  type: "DASHBOARD"
});

export const goToCustomers = (query) => ({
  type: "CUSTOMERS",
  payload: {
    query
  }
});

export const goToCustomer = () => ({
  type: "CUSTOMER"
});

export const goToCustomerNew = () => ({
  type: "CUSTOMER_NEW"
});

export const goToCustomerEdit = () => ({
  type: "CUSTOMER_EDIT"
});

export const goToContacts = () => ({
  type: "CONTACTS"
});

export const goToNewContact = () => ({
  type: "CONTACTS_NEW"
});

export const notFound = () => ({
  type: NOT_FOUND
});