import { combineReducers } from "redux";
import * as actions from "../actions";
import createFullEntity from "./creators/createFullEntity";

const { reducers, selectors } = createFullEntity({
  rootSelector: state => state.customers,

  createAction: actions.customerCreate,
  createFinishedAction: actions.customerCreateFinished,
  createErrorAction: actions.customerCreateError,

  editAction: actions.customerEdit,
  editFinishedAction: actions.customerEditFinished,
  editErrorAction: actions.customerEditError,

  deleteAction: actions.customersDelete,
  deleteFinishedAction: actions.customersDeleteFinished,
  deleteErrorAction: actions.customersDeleteError,

  loadSingleAction: actions.customerLoad,
  loadSingleFinishedAction: actions.customerLoadFinished,
  loadSingleErrorAction: actions.customerLoadError,

  loadPagedAction: actions.customersLoad,
  loadPagedFinishedAction: actions.customersLoadFinished,
  loadPagedErrorAction: actions.customersLoadError,
});

export const reducer = combineReducers({
  ...reducers
});

export default selectors;