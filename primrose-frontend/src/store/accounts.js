import { combineReducers } from "redux";
import * as actions from "../actions";
import * as location from "./location";
import createFullEntity from "./creators/createFullEntity";

const { reducers, selectors, async } = createFullEntity({
  entityName: "accounts",
  apiUrl: "/accounts",
  rootSelector: state => state.accounts,

  createAction: actions.accountCreate,
  createFinishedAction: actions.accountCreateFinished,
  createErrorAction: actions.accountCreateError,
  createApiParameters: ({
    action,
    state,
  }) => ({
    customer: parseInt(location.getCurrentData(state).customer, 10),
    account: action.payload
  }),

  editAction: actions.accountEdit,
  editFinishedAction: actions.accountEditFinished,
  editErrorAction: actions.accountEditError,
  editApiParameters: ({
    action,
    state
  }) => ({
    customer: parseInt(location.getCurrentData(state).customer, 10),
    account: action.payload
  }),

  deleteAction: actions.accountsDelete,
  deleteFinishedAction: actions.accountsDeleteFinished,
  deleteErrorAction: actions.accountsDeleteError,
  deleteApiParameters: ({
    action,
    state,
  }) => Array.isArray(action.payload.accounts) ? {
    customer: parseInt(location.getCurrentData(state).customer, 10),
    accounts: action.payload.accounts
  } : {
        customer: parseInt(location.getCurrentData(state).customer, 10),
        account: action.payload.accounts
      },

  loadSingleAction: actions.accountLoad,
  loadSingleFinishedAction: actions.accountLoadFinished,
  loadSingleErrorAction: actions.accountLoadError,
  loadSingleApiParameters: ({
    state
  }) => ({
    customer: parseInt(location.getCurrentData(state).customer, 10),
    account: parseInt(location.getCurrentData(state).account, 10)
  }),

  loadPagedAction: actions.accountsLoad,
  loadPagedFinishedAction: actions.accountsLoadFinished,
  loadPagedErrorAction: actions.accountsLoadError,
  loadPagedApiParameters: ({ state }) => ({
    customer: parseInt(location.getCurrentData(state).customer, 10),
    search: location.getCurrentPagination(state),
  }),
});

export const reducer = combineReducers({
  ...reducers
});

export const promise = async;
export default selectors;