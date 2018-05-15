import * as actions from "../actions";

const components = {
  [actions.dashboardPage]: "Dashboard",

  [actions.customerListPage]: "CustomerList",
  [actions.customerViewPage]: "CustomerView",
  [actions.customerUpdatePage]: "CustomerUpdate",
  [actions.customerNewPage]: "CustomerNew",

  [actions.contactListPage]: "ContactList",
  [actions.contactViewPage]: "ContactView",
  [actions.contactUpdatePage]: "ContactUpdate",
  [actions.contactNewPage]: "ContactNew",
  
  [actions.notFound]: "Error"
}

export const getPage = state => state.page;
export default (state = "", action = {}) => components[action.type] || state;