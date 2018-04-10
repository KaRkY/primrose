import {
  combineReducers
} from "redux";
import merge from "lodash/merge";
import getPageId from "../util/getPageId";

const byId = (state = {}, action) => {
  switch (action.type) {
    case "CONTACTS_FETCHED":
      return merge({}, state, action.payload.entities.contacts);

    default:
      return state;
  }
};

const pages = (state = {}, action) => {
  switch (action.type) {
    case "CONTACTS_FETCHED": {
      return merge({}, state, {
        [action.payload.pageId]: {
          result: action.payload.result.contacts,
          isLoading: false,
          loaded: true,
        }
      });
    }

    case "CONTACTS": {
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
    case "CONTACTS_FETCHED": {
      return action.payload.result.contactsCount;
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