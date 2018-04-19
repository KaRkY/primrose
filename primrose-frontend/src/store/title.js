import * as actions from "../actions";

const components = {
  [actions.dashboardPage]: "Dashboard",
  [actions.errorPage]: "Sample error page",
  [actions.customersPage]: "Customers",
  [actions.customerPage]: "Customer",
  [actions.customerPageNew]: "New Customer",
  [actions.customerPageEdit]: "Edit Customer",
  [actions.contactsPage]: "Contacts",
  [actions.contactPage]: "Contact",
  [actions.contactPageNew]: "New Contact",
  [actions.contactPageEdit]: "Edit Contact",
  [actions.notFound]: "404 Not found"
}

export const getTitle = state => state.title;
export default (state = "", action = {}) => components[action.type] || state;