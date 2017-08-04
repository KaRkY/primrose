import { createSelector } from "reselect";

const root = state => state.app;
const location = state => state.app.location;
const theme = createSelector(root, root => root.theme);
const drawerOpen = createSelector(root, root => root.drawerOpen);

export default {
  root,
  theme,
  drawerOpen,
  location,
};