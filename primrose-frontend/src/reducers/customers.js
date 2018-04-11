import { combineReducers } from "redux";

const data = (state = [], action) => {
  switch (action.type) {
    case "CUSTOMERS_FETCHED": {
      return action.payload.customers;
    }
    default:
      return state;
  }
};

const count = (state = 0, action) => {
  switch (action.type) {
    case "CUSTOMERS_FETCHED": {
      if(!action.payload) return state;
      return action.payload.count;
    }

    default:
      return state;
  }
};

const loading = (state = false, action) => {
  switch (action.type) {
    case "CUSTOMERS_FETCH": return true;
    case "CUSTOMERS_FETCHED": return false;
    case "CUSTOMERS_ERROR": return false;
    default: return state;
  }
};

const error = (state = false, action) => {
  switch (action.type) {
    case "CUSTOMERS_FETCHED": return false;
    case "CUSTOMERS_ERROR": return true;
    case "CUSTOMERS_FETCH": return false;
    default: return state;
  }
};

export default combineReducers({
  data,
  count,
  loading,
  error,
});