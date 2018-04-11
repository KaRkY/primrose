import React from "react";
import { connect } from "react-redux";
import compose from "recompose/compose";
import * as actions from "./actions";

import isLoading from "./selectors/isLoading";
import getPage from "./selectors/getPage";

import App from "./components/ChildConfigApp";
import IconButton from "material-ui/IconButton";
import MoreVert from "material-ui-icons/MoreVert";
import Tooltip from "material-ui/Tooltip";
import Nav from "./components/nav/ChildConfigNav"
import Switcher from "./components/Switcher";

const mapState = state => ({
  title: state.title,
  page: getPage(state),
  isLoading: isLoading(state),
  pathname: state.location.pathname,
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
          <Nav.Item exact to={actions.goToDashboard()} name="Dashboard" />
          <Nav.Item to={actions.goToCustomers()} name="Customers" />
          <Nav.Item to={actions.goToContacts()} name="Contacts" />
          <Nav.Item exact to={actions.goToError()} name="Error" />
        </Nav>
      </App.Navigation>

      <App.Content>
        <Switcher {...props} />
      </App.Content>
    </App>
  );

const EnhancedMain = enhance(Main);

export default EnhancedMain;