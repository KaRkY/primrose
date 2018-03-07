import React from "react";

import App from "./components/App";
import List from "material-ui/List";
import IconButton from "material-ui/IconButton";
import MoreVert from "material-ui-icons/MoreVert";
import Tooltip from "material-ui/Tooltip";
import Switcher from "./components/Switcher";

const Main = () => (
  <App>
    <App.Toolbar position="fixed" title={"test"}>
      <Tooltip title="Actions" enterDelay={300}>
        <IconButton
          variant="raised"
          color="inherit">
          <MoreVert />
        </IconButton>
      </Tooltip>
    </App.Toolbar>

    <App.Navigation>
    </App.Navigation>

    <App.Content>
      <Switcher />
    </App.Content>
  </App>
);

export default Main;