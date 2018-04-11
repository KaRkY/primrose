import { combineReducers } from "redux";

const data = (state = [], action) => {
  switch (action.type) {
    case "CONTACTS_FETCHED": {
      return action.payload.contacts;
    }
    default:
      return state;
  }
};

const count = (state = 0, action) => {
  switch (action.type) {
    case "CONTACTS_FETCHED": {
      if(!action.payload) return state;
      return action.payload.count;
    }

    default:
      return state;
  }
};

const loading = (state = false, action) => {
  switch (action.type) {
    case "CONTACTS_FETCH": return true;
    case "CONTACTS_FETCHED": return false;
    case "CONTACTS_ERROR": return false;
    default: return state;
  }
};

const error = (state = false, action) => {
  switch (action.type) {
    case "CONTACTS_FETCHED": return false;
    case "CONTACTS_ERROR": return true;
    case "CONTACTS_FETCH": return false;
    default: return state;
  }
};

export default combineReducers({
  data,
  count,
  loading,
  error,
});