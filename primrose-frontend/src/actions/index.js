import { NOT_FOUND } from "redux-first-router";
import {createAction } from "redux-actions";

export const dashboard = createAction("DASHBOARD");

export const customers = createAction("CUSTOMERS");
export const customer = createAction("CUSTOMER");
export const customersLoading = createAction("CUSTOMERS_LOADING");
export const customersFetched = createAction("CUSTOMERS_FETCHED");
export const customersError = createAction("CUSTOMERS_ERROR");
export const customerNew = createAction("CUSTOMER_NEW");
export const customerEdit = createAction("CUSTOMER_EDIT");
export const customerDelete = createAction("CUSTOMER_DELETE");
export const customerCreate = createAction("CUSTOMER_CREATE");

export const contacts = createAction("CONTACTS");
export const contact = createAction("CONTACT");
export const contactsLoading = createAction("CONTACTS_LOADING");
export const contactsFetched = createAction("CONTACTS_FETCHED");
export const contactsError = createAction("CONTACTS_ERROR");
export const contactNew = createAction("CONTACT_NEW");
export const contactEdit = createAction("CONTACT_EDIT");
export const contactDelete = createAction("CONTACT_DELETE");
export const contactCreate = createAction("CONTACT_CREATE");

export const error = createAction("ERROR");

export const notFound = createAction(NOT_FOUND);