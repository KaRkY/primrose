import {
  combineReducers
} from "redux";
import merge from "lodash/merge";
import getPageId from "../util/getPageId";

const byId = (state = {}, action) => {
  switch (action.type) {
    case "CUSTOMERS_FETCHED":
      return merge({}, state, action.payload.entities.customers);

    default:
      return state;
  }
};

const pages = (state = {}, action) => {
  switch (action.type) {
    case "CUSTOMERS_FETCHED": {
      return merge({}, state, {
        [action.payload.pageId]: {
          result: action.payload.result.customers,
          isLoading: false,
          loaded: true,
        }
      });
    }

    case "CUSTOMERS": {
      const page = state[getPageId(action.meta.query)];

      if(page && page.loaded) {
        return state;
      }

      return merge({}, state, {
        [getPageId(action.meta.query)]: {
          result: [],
          isLoading: true,
          loaded: false,
        }
      });
    }

    default:
      return state;
  }
};

const totalCount = (state = 0, action) => {
  switch (action.type) {
    case "CUSTOMERS_FETCHED": {
      return action.payload.result.customersCount;
    }

    default:
      return state;
  }
};

export default combineReducers({
  byId,
  pages,
  totalCount,
});