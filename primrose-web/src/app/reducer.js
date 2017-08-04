import { combineReducers } from "redux";
import { handleAction, handleActions } from "redux-actions";
import merge from "lodash/merge";

import deepOrange from "material-ui/colors/deepOrange";
import pink from "material-ui/colors/pink";

import AppActions from "./actions";
import AccountActions from "./accounts/actions";
import ContactsActions from "./contacts/actions";
import DashboardActions from "./dashboard/actions";
import accounts from "./accounts/reducer";

const location = location => ({
  next: state => location,
  throw: state => state
});

export default combineReducers({
  theme: handleAction(
    AppActions.types.THEME_CHANGE_REQUESTED, {
      next: (state, action) => merge({}, state, action.payload),
    }, {
      primary: deepOrange,
      accent: pink,
      type: "dark"
    }),

  drawerOpen: handleAction(
    AppActions.types.DRAWER_VISIBILITY_CHANGE_REQUESTED, {
      next: (state, action) => action.payload,
    }, false),

  location: handleActions({
    [AccountActions.types.ACCOUNTS_SCENE_REQUESTED]: location("accounts"),
    [AccountActions.types.ACCOUNTS_VIEW_SCENE_REQUESTED]: location("accounts.view"),
    [ContactsActions.types.CONTACTS_SCENE_REQUESTED]: location("contacts"),
    [DashboardActions.types.DASHBOARD_SCENE_REQUESTED]: location("dashboard"),
  }, ""),

  accounts,
});