import * as actions from "../actions";
import * as location from "./location";
import createFullEntity from "./creators/createFullEntity";

export default createFullEntity({
  entityName: "customers",
  apiUrl: "/customers",
  rootSelector: state => state.customers,

  createAction: actions.customerCreate,
  createFinishedAction: actions.customerCreateFinished,
  createErrorAction: actions.customerCreateError,
  createApiParameters: ({
    action
  }) => ({
    customer: action.payload
  }),

  editAction: actions.customerEdit,
  editFinishedAction: actions.customersEditFinished,
  editErrorAction: actions.customersEditError,
  editApiParameters: ({
    action
  }) => ({
    customer: action.payload
  }),

  deleteAction: actions.customersDelete,
  deleteFinishedAction: actions.customersDeleteFinished,
  deleteErrorAction: actions.customersDeleteError,
  deleteApiParameters: ({
    action
  }) => Array.isArray(action.payload.customers) ? {
    customers: action.payload.customers
  } : {
    customer: action.payload.customers
  },

  loadSingleAction: actions.customerLoad,
  loadSingleFinishedAction: actions.customerLoadFinished,
  loadSingleErrorAction: actions.customerLoadError,
  loadSingleApiParameters: ({
    state
  }) => ({
    customer: parseInt(location.getCurrentData(state).customer, 10)
  }),

  loadPagedAction: actions.customersLoad,
  loadPagedFinishedAction: actions.customersLoadFinished,
  loadPagedErrorAction: actions.customersLoadError,
  loadPagedApiParameters: ({ state }) => ({
    search: location.getCurrentPagination(state),
  }),
});