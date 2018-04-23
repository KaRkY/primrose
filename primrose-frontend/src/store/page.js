import * as actions from "../actions";

const components = {
  [actions.dashboardPage]: "PageDashboard",
  [actions.errorPage]: "ER",
  [actions.customersPage]: "customers/Paged",
  [actions.customerPage]: "customers/Single",
  [actions.customerPageNew]: "customers/New",
  [actions.customerPageEdit]: "customers/Edit",
  [actions.accountsPage]: "accounts/Paged",
  [actions.accountPage]: "accounts/Single",
  [actions.accountPageNew]: "accounts/New",
  [actions.accountPageEdit]: "accounts/Edit",
  [actions.contactsPage]: "contacts/Paged",
  [actions.contactPage]: "contacts/Single",
  [actions.contactPageNew]: "contacts/New",
  [actions.contactPageEdit]: "contacts/Edit",
  [actions.notFound]: "PageError"
}

export const getPage = state => state.page;
export default (state = "", action = {}) => components[action.type] || state;