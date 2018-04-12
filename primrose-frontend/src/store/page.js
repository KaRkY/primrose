import * as actions from "../actions";

const components = {
  [actions.dashboard]: "PageDashboard",
  [actions.error]: "ER",
  [actions.customers]: "PageCustomers",
  [actions.customer]: "PageCustomer",
  [actions.customerNew]: "PageNewCustomer",
  [actions.customerEdit]: "PageEditCustomer",
  [actions.contacts]: "PageContacts",
  [actions.contact]: "PageContact",
  [actions.contactNew]: "PageNewContact",
  [actions.contactEdit]: "PageEditContact",
  [actions.notFound]: "PageError"
}

export const getPage = state => state.page;
export default (state = "", action = {}) => components[action.type] || state;