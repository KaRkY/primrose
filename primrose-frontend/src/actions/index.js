import { NOT_FOUND } from "redux-first-router";
import { createAction } from "redux-actions";

const createCreateActions = entity => [
  createAction(`${entity}_CREATE`),
  createAction(`${entity}_CREATE_FINISHED`),
  createAction(`${entity}_CREATE_ERROR`)
];

const createEditActions = entity => [
  createAction(`${entity}_EDIT`),
  createAction(`${entity}_EDIT_FINISHED`),
  createAction(`${entity}_EDIT_ERROR`)
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
export const [customerCreate, customerCreateFinished, customerCreateError] = createCreateActions("CUSTOMER");
export const [customersLoad, customersLoadFinished, customersLoadError] = createLoadActions("CUSTOMERS");
export const [customerLoad, customerLoadFinished, customerLoadError] = createLoadActions("CUSTOMER");
export const [customersEdit, customersEditFinished, customersEditError] = createEditActions("CUSTOMER");
export const [customersDelete, customersDeleteFinished, customersDeleteError] = createDeleteActions("CUSTOMERS");


export const contacts = createAction("CONTACTS");
export const contact = createAction("CONTACT");
export const contactNew = createAction("CONTACT_NEW");
export const contactEdit = createAction("CONTACT_EDIT");
export const [contactCreate, contactCreateFinished, contactCreateError] = createCreateActions("CONTACT");
export const [contactLoad, contactLoadFinished, contactLoadError] = createLoadActions("CONTACT");
export const [contactsLoad, contactsLoadFinished, contactsLoadError] = createLoadActions("CONTACTS");
export const [contactsEdit, contactsEditFinished, contactsEditError] = createEditActions("CONTACT");
export const [contactsDelete, contactsDeleteFinished, contactsDeleteError] = createDeleteActions("CONTACT");

export const accounts = createAction("ACCOUNTS");
export const account = createAction("ACCOUNT");
export const accountNew = createAction("ACCOUNT_NEW");
export const accountEdit = createAction("ACCOUNT_EDIT");
export const [accountCreate, accountCreateFinished, accountCreateError] = createCreateActions("ACCOUNT");
export const [accountLoad, accountLoadFinished, accountLoadError] = createLoadActions("ACCOUNT");
export const [accountsLoad, accountsLoadFinished, accountsLoadError] = createLoadActions("ACCOUNTS");
export const [accountsEdit, accountsEditFinished, accountsEditError] = createEditActions("ACCOUNT");
export const [accountsDelete, accountsDeleteFinished, accountsDeleteError] = createDeleteActions("ACCOUNT");


export const [customerTypesLoad, customerTypesLoadFinished, customerTypesLoadError] = createLoadActions("CUSTOMER_TYPES");
export const [customerRelationTypesLoad, customerRelationTypesLoadFinished, customerRelationTypesLoadError] = createLoadActions("CUSTOMER_RELATION_TYPES");
export const [emailTypesLoad, emailTypesLoadFinished, emailTypesLoadError] = createLoadActions("EMAIL_TYPES");
export const [phoneNumberTypesLoad, phoneNumberTypesLoadFinished, phoneNumberTypesLoadError] = createLoadActions("PHONE_NUMBER_TYPES");

export const error = createAction("ERROR");

export const notFound = createAction(NOT_FOUND);