import axios from "../axios";
import convertError from "../util/convertError";
import createExecuteLifecycle from "./creators/createExecuteLifecycle";
import * as actions from "../actions";

const entity = createExecuteLifecycle({
  baseAction: actions.customersDelete,
  createdAction: actions.customersDeleteFinished,
  errorAction: actions.customersDeleteError,
  rootSelector: state => state.deleteCustomers,
});

export const apiDelete = ({
  dispatch,
  state,
  action
}) => {
  const customers = action.payload.customers;
  if (Array.isArray(customers)) {
    axios.post("/customers", {
        jsonrpc: "2.0",
        method: "delete",
        params: {
          customers,
        },
        id: Date.now(),
      })
      .then(result => dispatch(actions.customersDeleteFinished(result.data.result)))
      .catch(error => dispatch(actions.customersDeleteError(convertError(error))));
  } else {
    axios.post("/customers", {
        jsonrpc: "2.0",
        method: "delete",
        params: {
          customer: customers
        },
        id: Date.now(),
      })
      .then(result => dispatch(actions.customersDeleteFinished(result.data.result)))
      .catch(error => dispatch(actions.customersDeleteError(convertError(error))));
  }
};

export const getError = entity.selectors.getError;
export const isLoading = entity.selectors.isLoading;
export default entity.reducer;