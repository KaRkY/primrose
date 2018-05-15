import compose from "recompose/compose";
import withStateHandlers from "recompose/withStateHandlers";
import withMobile from "../withMobile";
import withTheme from "@material-ui/core/styles/withTheme";

export default compose(
  withMobile(),
  withTheme(),
  withStateHandlers(
    ({ mobile }) => ({ drawerOpen: !mobile }),
    {
      onDrawerOpen: ({ drawerOpen, ...props }) => () => ({ drawerOpen: true, ...props }),
      onDrawerClose: ({ drawerOpen, ...props }) => () => ({ drawerOpen: false, ...props }),
    }
  ),
);