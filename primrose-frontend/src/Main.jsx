import React from "react";
import { connect } from "react-redux";
import * as actions from "./actions";

import isLoading from "./store/isLoading";
import getError from "./store/getError";
import * as location from "./store/location";
import * as page from "./store/page";
import * as title from "./store/title";

import App from "./components/App";
import Nav from "./components/Nav"
import Switcher from "./pages/Switcher";

import compose from "recompose/compose";

import Tooltip from "@material-ui/core/Tooltip";
import IconButton from "@material-ui/core/IconButton";

import MoreVertIcon from "@material-ui/icons/MoreVert";

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
  <App
    toolbar={{
      title: props.title,
      actions: (
        <Tooltip title="Actions" enterDelay={300}>
          <IconButton
            variant="raised"
            color="inherit">
            <MoreVertIcon />
          </IconButton>
        </Tooltip>
      )
    }}

    navigation={
      <Nav
        items={[
          { exact: true, to: actions.dashboardPage(), name: "Dashboard" },
          { to: actions.customersPage({ force: true }), name: "Customers" },
          { to: actions.contactsPage({ force: true }), name: "Contacts" },
          { exact: true, to: actions.errorPage(), name: "Error" },
        ]}
      />
    }

    content={
      <Switcher {...props} />
    }
  />
);

const EnhancedMain = enhance(Main);

export default EnhancedMain;