import { combineReducers } from "redux";
import * as actions from "../actions";
import createFullEntity from "./creators/createFullEntity";

const { reducers, selectors, async } = createFullEntity({
  rootSelector: state => state.customers,

  createAction: actions.customerCreate,
  createFinishedAction: actions.customerCreateFinished,
  createErrorAction: actions.customerCreateError,

  editAction: actions.customerEdit,
  editFinishedAction: actions.customerEditFinished,
  editErrorAction: actions.customerEditError,

  deleteAction: actions.customersDeactivate,
  deleteFinishedAction: actions.customersDeactivateFinished,
  deleteErrorAction: actions.customersDeactivateError,

  loadSingleAction: actions.customerLoad,
  loadSingleFinishedAction: actions.customerLoadFinished,
  loadSingleErrorAction: actions.customerLoadError,

  loadPagedAction: actions.customerListLoad,
  loadPagedFinishedAction: actions.customerListFinished,
  loadPagedErrorAction: actions.customerListError,
});

export const reducer = combineReducers({
  ...reducers
});

export const promise = async;
export default selectors;