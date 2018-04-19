import * as actions from "../actions";

import customers from "./customers";
import contacts from "./contacts";
import meta from "./meta";

import * as location from "./location";

const components = {
  [actions.customersPage]: state =>
    customers.paged.isLoading(state) ||
    meta.customerTypes.isLoading(state) ||
    meta.customerRelationTypes.isLoading(state),

  [actions.customerPageNew]: state =>
    meta.customerTypes.isLoading(state) ||
    meta.customerRelationTypes.isLoading(state) ||
    meta.emailTypes.isLoading(state) ||
    meta.phoneNumberTypes.isLoading(state),

  [actions.contactPageNew]: state =>
    meta.emailTypes.isLoading(state) ||
    meta.phoneNumberTypes.isLoading(state),

  [actions.customerPage]: state =>
    customers.single.isLoading(state) ||
    meta.customerTypes.isLoading(state) ||
    meta.customerRelationTypes.isLoading(state) ||
    meta.emailTypes.isLoading(state) ||
    meta.phoneNumberTypes.isLoading(state),

  [actions.customerPageEdit]: state =>
    customers.single.isLoading(state) ||
    meta.customerTypes.isLoading(state) ||
    meta.customerRelationTypes.isLoading(state) ||
    meta.emailTypes.isLoading(state) ||
    meta.phoneNumberTypes.isLoading(state),

  [actions.contactsPage]: state =>
    contacts.paged.isLoading(state),

  [actions.contactPage]: state =>
    contacts.single.isLoading(state) ||
    meta.emailTypes.isLoading(state) ||
    meta.phoneNumberTypes.isLoading(state),
    
  [actions.contactPageEdit]: state =>
    contacts.single.isLoading(state) ||
    meta.emailTypes.isLoading(state) ||
    meta.phoneNumberTypes.isLoading(state),
}

export default state => {
  const fun = components[location.getPageType(state)];
  return fun ? fun(state) : false;
};