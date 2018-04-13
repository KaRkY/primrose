import { NOT_FOUND } from "redux-first-router";
import { createAction } from "redux-actions";

const createCreateActions = entity => [
  createAction(`${entity}_CREATE`),
  createAction(`${entity}_CREATE_FINISHED`),
  createAction(`${entity}_CREATE_ERROR`)
];

const createDeleteActions = entity => [
  createAction(`${entity}_DELETE`),
  createAction(`${entity}_DELETE_FINISHED`),
  createAction(`${entity}_DELETE_ERROR`)
];

const createLoadActions = entity => [
  createAction(`${entity}_LOAD`),
  createAction(`${entity}_LOAD_FINISHED`),
  createAction(`${entity}_LOAD_ERROR`)
];

export const dashboard = createAction("DASHBOARD");

export const customers = createAction("CUSTOMERS");
export const customer = createAction("CUSTOMER");
export const customerNew = createAction("CUSTOMER_NEW");
export const customerEdit = createAction("CUSTOMER_EDIT");
export const [customersLoad, customersLoadFinished, customersLoadError] = createLoadActions("CUSTOMERS");
export const [customersDelete, customersDeleteFinished, customersDeleteError] = createDeleteActions("CUSTOMERS");
export const [customerCreate, customerCreateFinished, customerCreateError] = createCreateActions("CUSTOMER");

export const contacts = createAction("CONTACTS");
export const contact = createAction("CONTACT");
export const contactNew = createAction("CONTACT_NEW");
export const contactEdit = createAction("CONTACT_EDIT");
export const [contactsLoad, contactsLoadFinished, contactsLoadError] = createLoadActions("CONTACTS");
export const [contactsDelete, contactsDeleteFinished, contactsDeleteError] = createDeleteActions("CONTACT");
export const [contactCreate, contactCreateFinished, contactCreateError] = createCreateActions("CONTACT");

export const error = createAction("ERROR");

export const notFound = createAction(NOT_FOUND);