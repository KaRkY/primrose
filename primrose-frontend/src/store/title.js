import * as actions from "../actions";
import { handleActions } from "redux-actions";

export const getTitle = state => state.title;

export default handleActions({
  [actions.dashboardPage]: () => "Dashboard",

  [actions.customerListPage]: () => "Customers",
  [actions.customerNewPage]: () => "New Customer",
  
  [actions.customerViewPage]: () => "Customer",
  [actions.customerViewFinished]: (state, action) => {
    if(action.payload) {
      if(action.payload.displayName) {
        return `Customer: ${action.payload.displayName}`;
      } else {
        return `Customer: ${action.payload.fullName}`;
      }
    } else {
      return "Customer";
    }
  },
  
  [actions.customerUpdatePage]: () => "Update Customer",
  [actions.customerUpdateFinished]: (state, action) => {
    if(action.payload) {
      if(action.payload.displayName) {
        return `Customer: ${action.payload.displayName}`;
      } else {
        return `Customer: ${action.payload.fullName}`;
      }
    } else {
      return "Customer";
    }
  },
  
  [actions.contactListPage]: () => "Contacts",
  [actions.contactViewPage]: () => "Contact",
  [actions.contactUpdatePage]: () => "Update Contact",
  [actions.contactNewPage]: () => "New Contact",

  [actions.markdownExamplePage]: () => "Markdown Example",

  [actions.notFound]: () => "404 Not found"
}, "Primrose");