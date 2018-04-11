import { NOT_FOUND } from "redux-first-router";

// try dispatching these from the redux devTools

export const goToPage = (type, category) => ({
  type,
  payload: category && { category }
});

export const goToDashboard = () => ({
  type: "DASHBOARD"
});

export const goToError = () => ({
  type: "ERROR"
});

export const goToCustomers = payload => ({
  type: "CUSTOMERS",
  payload
});

export const executeDeleteCustomers = payload => ({
  type: "CUSTOMERS_DELETE",
  payload,
});

export const goToCustomer = payload => ({
  type: "CUSTOMER",
  payload,
});

export const goToNewCustomer = () => ({
  type: "CUSTOMER_NEW"
});

export const goToEditCustomer = payload => ({
  type: "CUSTOMER_EDIT",
  payload,
});

export const goToContacts = payload => ({
  type: "CONTACTS",
  payload,
});

export const goToContact = payload => ({
  type: "CONTACT",
  payload,
});

export const goToNewContact = () => ({
  type: "CONTACT_NEW"
});

export const goToEditContact = () => ({
  type: "CONTACT_EDIT"
});

export const executeDeleteContacts = () => ({
  type: "CONTACTS_DELETE"
});

export const notFound = () => ({
  type: NOT_FOUND
});