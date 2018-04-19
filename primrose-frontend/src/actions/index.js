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

export const dashboardPage = createAction("DASHBOARD_PAGE");

export const customersPage = createAction("CUSTOMERS_PAGE");
export const customerPage = createAction("CUSTOMER_PAGE");
export const customerPageNew = createAction("CUSTOMER_PAGE_NEW");
export const customerPageEdit = createAction("CUSTOMER_PAGE_EDIT");
export const [customerCreate, customerCreateFinished, customerCreateError] = createCreateActions("CUSTOMER");
export const [customersLoad, customersLoadFinished, customersLoadError] = createLoadActions("CUSTOMERS");
export const [customerLoad, customerLoadFinished, customerLoadError] = createLoadActions("CUSTOMER");
export const [customerEdit, customerEditFinished, customerEditError] = createEditActions("CUSTOMER");
export const [customersDelete, customersDeleteFinished, customersDeleteError] = createDeleteActions("CUSTOMERS");


export const contactsPage = createAction("CONTACTS_PAGE");
export const contactPage = createAction("CONTACT_PAGE");
export const contactPageNew = createAction("CONTACT_PAGE_NEW");
export const contactPageEdit = createAction("CONTACT_PAGE_EDIT");
export const [contactCreate, contactCreateFinished, contactCreateError] = createCreateActions("CONTACT");
export const [contactLoad, contactLoadFinished, contactLoadError] = createLoadActions("CONTACT");
export const [contactsLoad, contactsLoadFinished, contactsLoadError] = createLoadActions("CONTACTS");
export const [contactEdit, contactEditFinished, contactEditError] = createEditActions("CONTACT");
export const [contactsDelete, contactsDeleteFinished, contactsDeleteError] = createDeleteActions("CONTACT");

export const accountsPage = createAction("ACCOUNTS_PAGE");
export const accountPage = createAction("ACCOUNT_PAGE");
export const accountPageNew = createAction("ACCOUNT_PAGE_NEW");
export const accountPageEdit = createAction("ACCOUNT_PAGE_EDIT");
export const [accountCreate, accountCreateFinished, accountCreateError] = createCreateActions("ACCOUNT");
export const [accountLoad, accountLoadFinished, accountLoadError] = createLoadActions("ACCOUNT");
export const [accountsLoad, accountsLoadFinished, accountsLoadError] = createLoadActions("ACCOUNTS");
export const [accountEdit, accountEditFinished, accountEditError] = createEditActions("ACCOUNT");
export const [accountsDelete, accountsDeleteFinished, accountsDeleteError] = createDeleteActions("ACCOUNT");


export const [customerTypesLoad, customerTypesLoadFinished, customerTypesLoadError] = createLoadActions("CUSTOMER_TYPES");
export const [customerRelationTypesLoad, customerRelationTypesLoadFinished, customerRelationTypesLoadError] = createLoadActions("CUSTOMER_RELATION_TYPES");
export const [emailTypesLoad, emailTypesLoadFinished, emailTypesLoadError] = createLoadActions("EMAIL_TYPES");
export const [phoneNumberTypesLoad, phoneNumberTypesLoadFinished, phoneNumberTypesLoadError] = createLoadActions("PHONE_NUMBER_TYPES");

export const errorPage = createAction("ERROR_PAGE");

export const notFound = createAction(NOT_FOUND);