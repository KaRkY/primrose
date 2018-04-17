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
  [actions.customers]: state => customers.isLoading(state) || customerTypes.isLoading(state) || customerRelationTypes.isLoading(state),
  [actions.contacts]: state => contacts.isLoading(state),
  [actions.customerNew]: state =>
    customerTypes.isLoading(state) ||
    customerRelationTypes.isLoading(state) ||
    emailTypes.isLoading(state) ||
    phoneNumberTypes.isLoading(state),
  [actions.contactNew]: state =>
    emailTypes.isLoading(state) ||
    phoneNumberTypes.isLoading(state),
  [actions.customer]: state =>
    customer.isLoading(state) ||
    customerTypes.isLoading(state) ||
    customerRelationTypes.isLoading(state) ||
    emailTypes.isLoading(state) ||
    phoneNumberTypes.isLoading(state),
  [actions.customerEdit]: state =>
    customer.isLoading(state) ||
    customerTypes.isLoading(state) ||
    customerRelationTypes.isLoading(state) ||
    emailTypes.isLoading(state) ||
    phoneNumberTypes.isLoading(state),
  [actions.contact]: state =>
    contact.isLoading(state) ||
    emailTypes.isLoading(state) ||
    phoneNumberTypes.isLoading(state),
  [actions.contactEdit]: state =>
    contact.isLoading(state) ||
    emailTypes.isLoading(state) ||
    phoneNumberTypes.isLoading(state),
}

export default state => {
  const fun = components[location.getPageType(state)];
  return fun ? fun(state) : false;
};