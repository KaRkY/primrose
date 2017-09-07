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
          search: undefined,
        },
        medium: {
          size: 16,
          page: 1,
          search: undefined,
        },
        large: {
          size: 24,
          page: 1,
          search: undefined,
        }
      }),
  }),

  app: combineReducers({
    accounts: combineReducers({
      search: combineReducers({
        paging: handleAction(
          SettingsActions.types.SETTINGS_UPDATE_REQUESTED, {
            next: (state, action) => action.payload,
          }, "medium"),
      }),
    }),
  }),

});