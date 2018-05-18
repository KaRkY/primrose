import React from "react";
import * as actions from "../../actions";

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

const Main = ({
  classes,
  title,
  ...props
}) => (
    <App
      toolbar={{
        title,
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
        <Switcher title={title} {...props} />
      }
    />
  );

export default Main;