import React from "react";
import { connect } from "react-redux";
import compose from "recompose/compose";
import * as actions from "./actions";

import App from "./components/ChildConfigApp";
import IconButton from "material-ui/IconButton";
import MoreVert from "material-ui-icons/MoreVert";
import Tooltip from "material-ui/Tooltip";
import Nav from "./components/nav/ChildConfigNav"
import Switcher from "./components/Switcher";

const mapState = ({ title }) => ({
  title,
});

const enhance = compose(
  connect(mapState),
);

const Main = ({
  title
}) => (
    <App>
      <App.Toolbar position="fixed" title={title}>
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
          <Nav.Item exact to={actions.goToDashboard()} name="Dashboard" loading />
          <Nav.Item to={actions.goToCustomers()} name="Customers" />
          <Nav.Item name="Test">
            <Nav.Item exact to={actions.goToContacts()} name="Dashboard" />
            <Nav.Item to={actions.goToContacts()} name="Customers" />
            <Nav.Item to={actions.goToContacts()} name="Contacts" />
          </Nav.Item>
          <Nav.Item to={actions.goToContacts()} name="Contacts" />
        </Nav>
      </App.Navigation>

      <App.Content>
        <Switcher />
      </App.Content>
    </App>
  );

const EnhancedMain = enhance(Main);

export default EnhancedMain;