import * as actions from "../actions";

const components = {
  [actions.dashboard]: "Dashboard",
  [actions.error]: "Sample error page",
  [actions.customers]: "Customers",
  [actions.customer]: "Customer",
  [actions.customerNew]: "New Customer",
  [actions.customerEdit]: "Edit Customer",
  [actions.contacts]: "Contacts",
  [actions.contact]: "Contact",
  [actions.contactNew]: "New Contact",
  [actions.contactEdit]: "Edit Contact",
  [actions.notFound]: "404 Not found"
}

export const getTitle = state => state.title;
export default (state = "", action = {}) => components[action.type] || state;