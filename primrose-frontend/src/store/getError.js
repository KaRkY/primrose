import * as actions from "../actions";

import customers from "./customers";
import contacts from "./contacts";
import meta from "./meta";

import * as location from "./location";

const components = {
  [actions.customers]: state =>
    customers.paged.getError(state) ||
    meta.customerTypes.getError(state) ||
    meta.customerRelationTypes.getError(state),

  [actions.customerNew]: state =>
    meta.customerTypes.getError(state) ||
    meta.customerRelationTypes.getError(state) ||
    meta.emailTypes.getError(state) ||
    meta.phoneNumberTypes.getError(state),

  [actions.contactNew]: state =>
    meta.emailTypes.getError(state) ||
    meta.phoneNumberTypes.getError(state),

  [actions.customer]: state =>
    customers.single.getError(state) ||
    meta.customerTypes.getError(state) ||
    meta.customerRelationTypes.getError(state) ||
    meta.emailTypes.getError(state) ||
    meta.phoneNumberTypes.getError(state),

  [actions.customerEdit]: state =>
    customers.single.getError(state) ||
    meta.customerTypes.getError(state) ||
    meta.customerRelationTypes.getError(state) ||
    meta.emailTypes.getError(state) ||
    meta.phoneNumberTypes.getError(state),

  [actions.contacts]: state =>
    contacts.paged.getError(state),

  [actions.contact]: state =>
    contacts.single.getError(state) ||
    meta.emailTypes.getError(state) ||
    meta.phoneNumberTypes.getError(state),
    
  [actions.contactEdit]: state =>
    contacts.single.getError(state) ||
    meta.emailTypes.getError(state) ||
    meta.phoneNumberTypes.getError(state),
}

export default state => {
  const fun = components[location.getPageType(state)];
  return fun && fun(state);
};