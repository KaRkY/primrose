import * as actions from "../actions";

import customers from "./customers";
import contacts from "./contacts";
import meta from "./meta";

import * as location from "./location";

const components = {
  [actions.customers]: state =>
    customers.paged.isLoading(state) ||
    meta.customerTypes.isLoading(state) ||
    meta.customerRelationTypes.isLoading(state),

  [actions.customerNew]: state =>
    meta.customerTypes.isLoading(state) ||
    meta.customerRelationTypes.isLoading(state) ||
    meta.emailTypes.isLoading(state) ||
    meta.phoneNumberTypes.isLoading(state),

  [actions.contactNew]: state =>
    meta.emailTypes.isLoading(state) ||
    meta.phoneNumberTypes.isLoading(state),

  [actions.customer]: state =>
    customers.single.isLoading(state) ||
    meta.customerTypes.isLoading(state) ||
    meta.customerRelationTypes.isLoading(state) ||
    meta.emailTypes.isLoading(state) ||
    meta.phoneNumberTypes.isLoading(state),

  [actions.customerEdit]: state =>
    customers.single.isLoading(state) ||
    meta.customerTypes.isLoading(state) ||
    meta.customerRelationTypes.isLoading(state) ||
    meta.emailTypes.isLoading(state) ||
    meta.phoneNumberTypes.isLoading(state),

  [actions.contacts]: state =>
    contacts.paged.isLoading(state),

  [actions.contact]: state =>
    contacts.single.isLoading(state) ||
    meta.emailTypes.isLoading(state) ||
    meta.phoneNumberTypes.isLoading(state),
    
  [actions.contactEdit]: state =>
    contacts.single.isLoading(state) ||
    meta.emailTypes.isLoading(state) ||
    meta.phoneNumberTypes.isLoading(state),
}

export default state => {
  const fun = components[location.getPageType(state)];
  return fun ? fun(state) : false;
};