import { combineReducers } from "redux";
import * as actions from "../actions";
import * as location from "./location";
import createFullEntity from "./creators/createFullEntity";

const { reducers, selectors, async } = createFullEntity({
  entityName: "contacts",
  apiUrl: "/contacts",
  rootSelector: state => state.contacts,

  createAction: actions.contactCreate,
  createFinishedAction: actions.contactCreateFinished,
  createErrorAction: actions.contactCreateError,
  createApiParameters: ({
    action
  }) => ({
    contact: action.payload
  }),

  editAction: actions.contactEdit,
  editFinishedAction: actions.contactEditFinished,
  editErrorAction: actions.contactEditError,
  editApiParameters: ({
    action
  }) => ({
    contact: action.payload
  }),

  deleteAction: actions.contactsDeactivate,
  deleteFinishedAction: actions.contactsDeactivateFinished,
  deleteErrorAction: actions.contactsDeactivateError,
  deleteApiParameters: ({
    action
  }) => Array.isArray(action.payload.contacts) ? {
    contacts: action.payload.contacts
  } : {
        contact: action.payload.contacts
      },

  loadSingleAction: actions.contactLoad,
  loadSingleFinishedAction: actions.contactLoadFinished,
  loadSingleErrorAction: actions.contactLoadError,
  loadSingleApiParameters: ({
    state
  }) => ({
    contact: parseInt(location.getCurrentData(state).contact, 10)
  }),

  loadPagedAction: actions.contactsLoad,
  loadPagedFinishedAction: actions.contactsLoadFinished,
  loadPagedErrorAction: actions.contactsLoadError,
  loadPagedApiParameters: ({ state }) => ({
    search: location.getCurrentPagination(state),
  }),
});

export const reducer = combineReducers({
  ...reducers
});

export const promise = async;
export default selectors;