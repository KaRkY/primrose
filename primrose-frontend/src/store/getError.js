import * as actions from "../actions";
import * as customers from "./customers";
import * as customer from "./customer";
import * as customerTypes from "./customerTypes";
import * as customerRelationTypes from "./customerRelationTypes";
import * as emailTypes from "./emailTypes";
import * as phoneNumberTypes from "./phoneNumberTypes";
import * as contacts from "./contacts";
import * as contact from "./contact";
import * as location from "./location";

const components = {
  [actions.customers]: state => customers.getError(state) || customerTypes.getError(state) || customerRelationTypes.getError(state),
  [actions.contacts]: state => contacts.getError(state),
  [actions.customerNew]: state =>
    customerTypes.getError(state) ||
    customerRelationTypes.getError(state) ||
    emailTypes.getError(state) ||
    phoneNumberTypes.getError(state),
  [actions.contactNew]: state =>
    emailTypes.getError(state) ||
    phoneNumberTypes.getError(state),
  [actions.customer]: state =>
    customer.getError(state) ||
    customerTypes.getError(state) ||
    customerRelationTypes.getError(state) ||
    emailTypes.getError(state) ||
    phoneNumberTypes.getError(state),
  [actions.customerEdit]: state =>
    customer.getError(state) ||
    customerTypes.getError(state) ||
    customerRelationTypes.getError(state) ||
    emailTypes.getError(state) ||
    phoneNumberTypes.getError(state),
  [actions.contact]: state =>
    contact.getError(state) ||
    emailTypes.getError(state) ||
    phoneNumberTypes.getError(state),
  [actions.contactEdit]: state =>
    contact.getError(state) ||
    emailTypes.getError(state) ||
    phoneNumberTypes.getError(state),
}

export default state => {
  const fun = components[location.getPageType(state)];
  return fun && fun(state);
};