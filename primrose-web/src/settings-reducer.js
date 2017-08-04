import { combineReducers } from "redux";
import { handleAction } from "redux-actions";

import SettingsActions from "./settings-actions";

export default combineReducers({
  defaults: combineReducers({
    query: handleAction(
      SettingsActions.types.SETTINGS_UPDATE_REQUESTED, {
        next: (state, action) => action.payload,
      }, {
        small: {
          size: 8,
          page: 1,
          search: null,
        },
        medium: {
          size: 16,
          page: 1,
          search: null,
        },
        large: {
          size: 24,
          page: 1,
          search: null,
        }
      }),
  }),

  app: combineReducers({
    accounts: combineReducers({
      search: combineReducers({
        paging: handleAction(
          SettingsActions.types.SETTINGS_UPDATE_REQUESTED, {
            next: (state, action) => action.payload,
          }, "large"),
      }),
    }),
  }),

});