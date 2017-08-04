import { createAction } from "redux-actions";
import pickBy from "lodash/pickBy";
import set from "lodash/set";

const types = {
  ACCOUNTS_SCENE_REQUESTED: "accounts/ACCOUNTS_SCENE_REQUESTED",
  ACCOUNTS_VIEW_SCENE_REQUESTED: "accounts/ACCOUNTS_VIEW_SCENE_REQUESTED",
  NEXT_PAGE_REQUESTED: "accounts/NEXT_PAGE_REQUESTED",
  PREVIOUS_PAGE_REQUESTED: "accounts/PREVIOUS_PAGE_REQUESTED",
  LOAD_ACCOUNTS_PAGE_REQUESTED: "accounts/LOAD_ACCOUNTS_PAGE_REQUESTED",
  LOAD_ACCOUNTS_PAGE_RESPONDED: "accounts/LOAD_ACCOUNTS_PAGE_RESPONDED"
};

const creators = {
  requestAccountsScene: createAction(
    types.ACCOUNTS_SCENE_REQUESTED,
    payload => payload ? { query: payload } : {},
  ),
  requestAccountsViewScene: createAction(types.ACCOUNTS_VIEW_SCENE_REQUESTED),
  requestLoadAccountsPage: createAction(
    types.LOAD_ACCOUNTS_PAGE_REQUESTED,
    payload => payload && { query: payload }),
  respondLoadAccountsPage: createAction(
    types.LOAD_ACCOUNTS_PAGE_RESPONDED,
    payload => payload.data,
    payload => ({ headers: pickBy(payload.headers, (val, key) => key.startsWith("search-")) })),
};


export default {
  types,
  creators
};