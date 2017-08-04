import { combineReducers } from "redux";
import { handleAction, handleActions } from "redux-actions";
import queryString from "query-string";

import AccountActions from "./actions";
import setWith from "lodash/setWith";
import cloneDeep from "lodash/cloneDeep";
import moment from "moment";


export default combineReducers({
  search: combineReducers({

    loading: handleActions({
      [AccountActions.types.LOAD_ACCOUNTS_PAGE_REQUESTED]: () => true,
      [AccountActions.types.LOAD_ACCOUNTS_PAGE_RESPONDED]: {
        next: () => false,
        throw: () => false,
      },
    }, false),

    error: handleActions({
      [AccountActions.types.LOAD_ACCOUNTS_PAGE_REQUESTED]: () => null,
      [AccountActions.types.LOAD_ACCOUNTS_PAGE_RESPONDED]: {
        next: () => null,
        throw: (state, action) => action.payload,
      },
    }, null),

    headers: handleAction(AccountActions.types.LOAD_ACCOUNTS_PAGE_RESPONDED, (state, action) => !action.error && action.meta.headers, {}),

    pages: handleAction(
      AccountActions.types.LOAD_ACCOUNTS_PAGE_RESPONDED, {
        // Clone whole state and set current page results
        next: (state, action) => {
          let key = queryString.stringify({
            size: action.meta.headers["search-requested-page-size"],
            page: action.meta.headers["search-page-number"],
            search: action.meta.headers["search-query"],
          });
          let result = setWith(
            cloneDeep(state),
            `${key}.results`,
            action.payload.result,
            Object);
          setWith(
            result,
            `${key}.lastUpdated`,
            moment(),
            Object);
          return result;
        },
      }, {})
  })
});