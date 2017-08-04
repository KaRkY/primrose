import { createAction } from "redux-actions";

const types = {
  DASHBOARD_SCENE_REQUESTED: "dashboard/DASHBOARD_SCENE_REQUESTED",
};

const creators = {
  requestDashboardScene: createAction(types.DASHBOARD_SCENE_REQUESTED),
};


export default {
  types,
  creators
};