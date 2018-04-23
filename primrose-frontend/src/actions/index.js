import { NOT_FOUND } from "redux-first-router";
import { createAction } from "redux-actions";
import promiseListener from "../store/promiseListener";

const createCreateActions = entity => {
  const create = createAction(`${entity}_CREATE`);
  const createFinished = createAction(`${entity}_CREATE_FINISHED`);
  const createError = createAction(`${entity}_CREATE_ERROR`);
  const self = {};

  return [
    create,
    createFinished,
    createError,
    (function () {
      if (!this.promiseListener) this.promiseListener = promiseListener({
        start: create,
        resolve: createFinished,
        reject: createError,
      });

      return this.promiseListener.asyncFunction(...arguments);
    }).bind(self),
  ];
};

const createEditActions = entity => {
  const edit = createAction(`${entity}_EDIT`);
  const editFinished = createAction(`${entity}_EDIT_FINISHED`);
  const editError = createAction(`${entity}_EDIT_ERROR`);
  const self = {};

  return [
    edit,
    editFinished,
    editError,
    (function () {
      if (!this.promiseListener) this.promiseListener = promiseListener({
        start: edit,
        resolve: editFinished,
        reject: editError,
      });

      return this.promiseListener.asyncFunction(...arguments);
    }).bind(self),
  ];
};

const createDeactivateActions = (entity) => {
  const del = createAction(`${entity}_DEACTIVATE`);
  const delFinished = createAction(`${entity}_DEACTIVATE_FINISHED`);
  const delError = createAction(`${entity}_DEACTIVATE_ERROR`);
  const self = {};

  return [
    del,
    delFinished,
    delError,
    (function () {
      if (!this.promiseListener) this.promiseListener = promiseListener({
        start: del,
        resolve: delFinished,
        reject: delError,
      });

      return this.promiseListener.asyncFunction(...arguments);
    }).bind(self),
  ];
};

const createLoadActions = entity => {
  const load = createAction(`${entity}_LOAD`);
  const loadFinished = createAction(`${entity}_LOAD_FINISHED`);
  const loadError = createAction(`${entity}_LOAD_ERROR`);
  const self = {};

  return [
    load,
    loadFinished,
    loadError,
    (function () {
      if (!this.promiseListener) this.promiseListener = promiseListener({
        start: load,
        resolve: loadFinished,
        reject: loadError,
      });

      return this.promiseListener.asyncFunction(...arguments);
    }).bind(self),
  ];
};

export const dashboardPage = createAction("DASHBOARD_PAGE");

export const customersPage = createAction("CUSTOMERS_PAGE", ({ force, ...query } = {}) => ({ query, force }));
export const customerPage = createAction("CUSTOMER_PAGE", customer => ({ customer }));
export const customerPageNew = createAction("CUSTOMER_PAGE_NEW");
export const customerPageEdit = createAction("CUSTOMER_PAGE_EDIT", customer => ({ customer }));
export const [customerCreate, customerCreateFinished, customerCreateError, customerCreatePromise] = createCreateActions("CUSTOMER");
export const [customersLoad, customersLoadFinished, customersLoadError, customersLoadPromise] = createLoadActions("CUSTOMERS");
export const [customerCreateContactsLoad, customerCreateContactsFinished, customerCreateContactsError, customerCreateContactsPromise] = createLoadActions("CUSTOMER_CREATE_CONTACTS");
export const [customerLoad, customerLoadFinished, customerLoadError, customerLoadPromise] = createLoadActions("CUSTOMER");
export const [customerEdit, customerEditFinished, customerEditError, customerEditPromise] = createEditActions("CUSTOMER");
export const [customersDeactivate, customersDeactivateFinished, customersDeactivateError, customerDeactivatePromise] = createDeactivateActions("CUSTOMERS", "customers");


export const contactsPage = createAction("CONTACTS_PAGE", ({ force, ...query } = {}) => ({ query, force }));
export const contactPage = createAction("CONTACT_PAGE", contact => ({ contact }));
export const contactPageNew = createAction("CONTACT_PAGE_NEW");
export const contactPageEdit = createAction("CONTACT_PAGE_EDIT", contact => ({ contact }));
export const [contactCreate, contactCreateFinished, contactCreateError, contactCreatePromise] = createCreateActions("CONTACT");
export const [contactLoad, contactLoadFinished, contactLoadError, contactsLoadPromise] = createLoadActions("CONTACT");
export const [contactsLoad, contactsLoadFinished, contactsLoadError, contactLoadPromise] = createLoadActions("CONTACTS");
export const [contactEdit, contactEditFinished, contactEditError, contactEditPromise] = createEditActions("CONTACT");
export const [contactsDeactivate, contactsDeactivateFinished, contactsDeactivateError, contactDeactivatePromise] = createDeactivateActions("CONTACT", "contacts");

export const accountsPage = createAction("ACCOUNTS_PAGE", (customer, { force, ...query } = {}) => ({ customer, query, force }));
export const accountPage = createAction("ACCOUNT_PAGE", (customer, account) => ({ customer, account }));
export const accountPageNew = createAction("ACCOUNT_PAGE_NEW", (customer) => ({ customer }));
export const accountPageEdit = createAction("ACCOUNT_PAGE_EDIT", (customer, account) => ({ customer, account }));
export const [accountCreate, accountCreateFinished, accountCreateError, accountCreatePromise] = createCreateActions("ACCOUNT");
export const [accountLoad, accountLoadFinished, accountLoadError, accountsLoadPromise] = createLoadActions("ACCOUNT");
export const [accountsLoad, accountsLoadFinished, accountsLoadError, accountLoadPromise] = createLoadActions("ACCOUNTS");
export const [accountEdit, accountEditFinished, accountEditError, accountEditPromise] = createEditActions("ACCOUNT");
export const [accountsDeactivate, accountsDeactivateFinished, accountsDeactivateError, accountDeactivatePromise] = createDeactivateActions("ACCOUNT", "accounts");


export const [customerTypesLoad, customerTypesLoadFinished, customerTypesLoadError] = createLoadActions("CUSTOMER_TYPES");
export const [customerRelationTypesLoad, customerRelationTypesLoadFinished, customerRelationTypesLoadError] = createLoadActions("CUSTOMER_RELATION_TYPES");
export const [emailTypesLoad, emailTypesLoadFinished, emailTypesLoadError] = createLoadActions("EMAIL_TYPES");
export const [phoneNumberTypesLoad, phoneNumberTypesLoadFinished, phoneNumberTypesLoadError] = createLoadActions("PHONE_NUMBER_TYPES");

export const errorPage = createAction("ERROR_PAGE");

export const notFound = createAction(NOT_FOUND);