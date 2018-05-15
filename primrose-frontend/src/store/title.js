import * as actions from "../actions";

const components = {
  [actions.dashboardPage]: "Dashboard",

  [actions.customerListPage]: "Customers",
  [actions.customerViewPage]: "Customer",
  [actions.customerUpdatePage]: "Update Customer",
  [actions.customerNewPage]: "New Customer",

  [actions.contactListPage]: "Contacts",
  [actions.contactViewPage]: "Contact",
  [actions.contactUpdatePage]: "Update Contact",
  [actions.contactNewPage]: "New Contact",

  [actions.notFound]: "404 Not found"
}

export const getTitle = state => state.title;
export default (state = "", action = {}) => components[action.type] || state;