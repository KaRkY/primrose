import * as actions from "../actions";

import customers from "./customers";
import contacts from "./contacts";
import meta from "./meta";

import * as location from "./location";

const components = {
   [actions.customersPage]: state =>
      customers.paged.getError(state) ||
      meta.customerTypes.getError(state) ||
      meta.customerRelationTypes.getError(state),

   [actions.customerPageNew]: state =>
      meta.customerTypes.getError(state) ||
      meta.customerRelationTypes.getError(state) ||
      meta.emailTypes.getError(state) ||
      meta.phoneNumberTypes.getError(state),

   [actions.contactPageNew]: state =>
      meta.emailTypes.getError(state) ||
      meta.phoneNumberTypes.getError(state),

   [actions.customerPage]: state =>
      customers.single.getError(state) ||
      meta.customerTypes.getError(state) ||
      meta.customerRelationTypes.getError(state) ||
      meta.emailTypes.getError(state) ||
      meta.phoneNumberTypes.getError(state),

   [actions.customerPageEdit]: state =>
      customers.single.getError(state) ||
      meta.customerTypes.getError(state) ||
      meta.customerRelationTypes.getError(state) ||
      meta.emailTypes.getError(state) ||
      meta.phoneNumberTypes.getError(state),

   [actions.contactsPage]: state =>
      contacts.paged.getError(state),

   [actions.contactPage]: state =>
      contacts.single.getError(state) ||
      meta.emailTypes.getError(state) ||
      meta.phoneNumberTypes.getError(state),

   [actions.contactPageEdit]: state =>
      contacts.single.getError(state) ||
      meta.emailTypes.getError(state) ||
      meta.phoneNumberTypes.getError(state),

}

export default state => {
   const fun = components[location.getPageType(state)];
   return fun && fun(state);
};