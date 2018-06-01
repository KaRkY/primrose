import React from "react";
import * as actions from "../../actions";
import * as location from "../../store/location";
import * as page from "../../store/page";
import * as title from "../../store/title";

import App from "../../components/App";
import Nav from "../../components/Nav"
import PopupPanel from "../../components/PopupPanel";
import Switcher from "../Switcher";

import Avatar from "@material-ui/core/Avatar";
import IconButton from "@material-ui/core/IconButton";
import Typography from "@material-ui/core/Typography";

import AccountBoxIcon from "@material-ui/icons/AccountBox";
import AccountCircleIcon from "@material-ui/icons/AccountCircle";
import ContactsIcon from "@material-ui/icons/Contacts";
import DashboardIcon from "@material-ui/icons/Dashboard";

import Connect from "../../components/Connect";

const Main = ({ classes }) => (
  <Connect mapStateToProps={state => ({
    title: title.getTitle(state),
    page: page.getComponent(state),
    pageType: location.getPageType(state),
    isLoading: page.isLoading(state),
    error: page.getError(state),
    pathname: location.getCurrentPathname(state),
  })}>
    {state => (
      <App
        toolbar={{
          title: state.title,
          actions: (
            <PopupPanel
              className={classes.profilePanel}
              component={props => (
                <IconButton
                  variant="raised"
                  color="inherit"
                  {...props}
                >
                  <AccountCircleIcon />
                </IconButton>
              )}
            >
              <Avatar>RS</Avatar>
              <Typography>Delaaaaaaaaaaaa</Typography>
            </PopupPanel>
          )
        }}

        navigation={
          <Nav
            items={[
              { to: actions.dashboardPage(), name: "Dashboard", exact: true, icon: <DashboardIcon />, },
              { to: actions.customerListPage({ force: true }), name: "Customers", icon: <AccountBoxIcon />, },
              { to: actions.contactListPage({ force: true }), name: "Contacts", icon: <ContactsIcon />, },
              { separator: true },
              { to: actions.markdownExamplePage({ force: true }), name: "Markdown example", },
            ]}
          />
        }

        content={
          <Switcher {...state} />
        }
      />
    )}
  </Connect>
);

export default Main;