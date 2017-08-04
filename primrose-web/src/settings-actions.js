import { createAction } from "redux-actions";

const types = {
  SETTINGS_UPDATE_REQUESTED: "settings/SETTINGS_UPDATE_REQUESTED",
};

const creators = {
  requestSettingsUpdate: createAction(types.SETTINGS_UPDATE_REQUESTED),
};


export default {
  types,
  creators
};