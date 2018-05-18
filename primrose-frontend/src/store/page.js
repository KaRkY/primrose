import { combineReducers } from "redux";
import { createSelector } from "reselect";
import { handleActions } from "redux-actions";
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

  [actions.markdownExamplePage]: "MarkdownExample",
  
  [actions.notFound]: "Error"
}

const component = (state = "", action = {}) => components[action.type] || state;

const loading = handleActions({
  [actions.pageLoad]: (state, action) => !!!action.payload,
  [actions.pageFinished]: () => false,
  [actions.pageError]: () => false,
}, false);

const error = handleActions({
  [actions.pageLoad]: () => null,
  [actions.pageFinished]: () => null,
  [actions.pageError]: (state, action) => action.payload,
}, null);

export const rootSelector = state => state.page;
export const getComponent = createSelector(rootSelector, root => root.component);
export const isLoading = createSelector(rootSelector, root => root.loading);
export const getError = createSelector(rootSelector, root => root.error);
export default combineReducers({
  component,
  loading,
  error,
});