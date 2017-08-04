import { createAction } from "redux-actions";

const types = {
  ROOT_SCENE_REQUESTED: "ROOT_SCENE_REQUESTED",
  THEME_CHANGE_REQUESTED: "THEME_CHANGE_REQUESTED",
  DRAWER_VISIBILITY_CHANGE_REQUESTED: "DRAWER_VISIBILITY_CHANGE_REQUESTED",
};

const creators = {
  requestRootScene: createAction(types.ROOT_SCENE_REQUESTED),
  requestThemeChange: createAction(types.THEME_CHANGE_REQUESTED),
  requestDrawerVisibilityChange: createAction(types.DRAWER_VISIBILITY_CHANGE_REQUESTED),
  requestDrawerClose: createAction(
    types.DRAWER_VISIBILITY_CHANGE_REQUESTED, 
    () => false
  ),
  requestDrawerOpen: createAction(
    types.DRAWER_VISIBILITY_CHANGE_REQUESTED, 
    () => true
  ),
};


export default {
  types,
  creators
};