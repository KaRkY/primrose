import * as actions from "../actions";

const components = {
  [actions.dashboard]: "PageDashboard",
  [actions.error]: "ER",
  [actions.customers]: "customers/Paged",
  [actions.customer]: "customers/Single",
  [actions.customerNew]: "customers/New",
  [actions.customerEdit]: "customers/Edit",
  [actions.accounts]: "accounts/Paged",
  [actions.account]: "accounts/Single",
  [actions.accountNew]: "accounts/New",
  [actions.accountEdit]: "accounts/Edit",
  [actions.contacts]: "contacts/Paged",
  [actions.contact]: "contacts/Single",
  [actions.contactNew]: "contacts/New",
  [actions.contactEdit]: "contacts/Edit",
  [actions.notFound]: "PageError"
}

export const getPage = state => state.page;
export default (state = "", action = {}) => components[action.type] || state;