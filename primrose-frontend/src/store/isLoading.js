import * as actions from "../actions";
import * as customers from "./customers";
import * as contacts from "./contacts";
import * as location from "./location";

const components = {
  [actions.customers]: state => customers.isLoading(state),
  [actions.contacts]: state => contacts.isLoading(state),
}

export default state => {
  const fun = components[location.getPageType(state)];
  return fun ? fun(state) : false;
};