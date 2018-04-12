import React from "react";
import { connect } from "react-redux";
import compose from "recompose/compose";
import * as actions from "./actions";

import isLoading from "./store/isLoading";
import getError from "./store/getError";
import * as location from "./store/location";
import * as page from "./store/page";
import * as title from "./store/title";

import App from "./components/ChildConfigApp";
import IconButton from "material-ui/IconButton";
import MoreVert from "material-ui-icons/MoreVert";
import Tooltip from "material-ui/Tooltip";
import Nav from "./components/nav/ChildConfigNav"
import Switcher from "./components/Switcher";

const mapState = state => ({
  title: title.getTitle(state),
  page: page.getPage(state),
  pageType: location.getPageType(state),
  isLoading: isLoading(state),
  error: getError(state),
  pathname: location.getCurrentPathname(state),
});

const enhance = compose(
  connect(mapState),
);

const Main = props => (
    <App>
      <App.Toolbar position="fixed" title={props.title}>
        <Tooltip title="Actions" enterDelay={300}>
          <IconButton
            variant="raised"
            color="inherit">
            <MoreVert />
          </IconButton>
        </Tooltip>
      </App.Toolbar>

      <App.Navigation>
        <Nav>
          <Nav.Item exact to={actions.dashboard()} name="Dashboard" />
          <Nav.Item to={actions.customers({ force: true })} name="Customers" />
          <Nav.Item to={actions.contacts()} name="Contacts" />
          <Nav.Item exact to={actions.error()} name="Error" />
        </Nav>
      </App.Navigation>

      <App.Content>
        <Switcher {...props} />
      </App.Content>
    </App>
  );

const EnhancedMain = enhance(Main);

export default EnhancedMain;