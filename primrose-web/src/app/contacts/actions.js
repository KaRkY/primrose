import { createAction } from "redux-actions";

const types = {
  CONTACTS_SCENE_REQUESTED: "contacts/CONTACTS_SCENE_REQUESTED",
  CONTACTS_SEARCH_VIEW_REQUESTED: "contacts/CONTACTS_SEARCH_VIEW_REQUESTED",
};

const creators = {
  requestContactsScene: createAction(types.CONTACTS_SCENE_REQUESTED, () => {}),
  requestContactsViewScene: createAction(types.CONTACTS_SEARCH_VIEW_REQUESTED),
};


export default {
  types,
  creators
};