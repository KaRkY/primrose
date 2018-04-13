import axios from "../axiosInstance";
import convertError from "../util/convertError";
import createExecuteLifecycle from "./createExecuteLifecycle";
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
    axios.delete(`/customers`, { params: { customers }})
      .then(result => dispatch(actions.customersDeleteFinished(result.data)))
      .catch(error => dispatch(actions.customersDeleteError(convertError(error))));
  } else {
    axios.delete(`/customers/${customers}`)
      .then(result => dispatch(actions.customersDeleteFinished(result.data)))
      .catch(error => dispatch(actions.customersDeleteError(convertError(error))));
  }
};

export const getError = entity.selectors.getError;
export const isLoading = entity.selectors.isLoading;
export default entity.reducer;