import React from "react";
import PropTypes from "prop-types";
import { bindActionCreators } from "redux";
import { connect } from "react-redux";
import compose from "recompose/compose";
import { createStructuredSelector } from "reselect";
import classnames from "classnames";
import withHandlers from "recompose/withHandlers";

import { Navbar, NavbarBrand, NavbarItem, NavbarBurger } from "@/components/navbar";
import { Menu, MenuItem, MenuLabel, MenuList, MenuLink } from "@/components/menu";

import AppSelectors from "../selectors";
import AppActions from "../actions";
import DashboardActions from "../dashboard/actions";
import AccountsActions from "../accounts/actions";
import ContactsActions from "../contacts/actions";


const Frame = ({
  drawerOpen,
  toggleDrawer,
  onRequestDrawerClose,
  onRequestDashboard,
  onRequestAccounts,
  onRequestContacts
}) => (
    <div className="wrapper">
      <header className="header">
        <Navbar shadow>
          <NavbarBrand>
            <NavbarItem>
              <img src="http://bulma.io/images/bulma-logo.png" alt="Bulma: a modern CSS framework based on Flexbox" width="112" height="28" />
            </NavbarItem>

            <NavbarBurger onClick={toggleDrawer} />
          </NavbarBrand>
        </Navbar>
      </header>
      <aside className={classnames({
        "sidebar": true,
        "is-open": drawerOpen
      })}>
        <div className="sidebar-panel">
          <Menu>
            <div>
              <MenuLabel>General</MenuLabel>
              <MenuList>
                <MenuItem>
                  <MenuLink onClick={() => { onRequestDashboard(); onRequestDrawerClose() }}>Dashboard</MenuLink>
                </MenuItem>
                <MenuItem>
                  <MenuLink onClick={() => { onRequestAccounts(); onRequestDrawerClose() }}>Accounts</MenuLink>
                </MenuItem>
                <MenuItem>
                  <MenuLink onClick={() => { onRequestContacts(); onRequestDrawerClose() }}>Contacts</MenuLink>
                </MenuItem>
              </MenuList>
            </div>
          </Menu>
        </div>
        <div className="sidebar-overlay" onClick={onRequestDrawerClose} />
      </aside>
      <main className="main">Main</main>
    </div>
  )

Frame.propTypes = {
};

export default compose(
  connect(
    createStructuredSelector({
      drawerOpen: AppSelectors.drawerOpen
    }),
    dispatch => bindActionCreators({
      onRequestDrawerClose: AppActions.creators.requestDrawerClose,
      onRequestDrawerOpen: AppActions.creators.requestDrawerOpen,
      onRequestDashboard: DashboardActions.creators.requestDashboardScene,
      onRequestAccounts: AccountsActions.creators.requestAccountsScene,
      onRequestContacts: ContactsActions.creators.requestContactsScene,
    }, dispatch)
  ),
  withHandlers({
    toggleDrawer: ({ drawerOpen, onRequestDrawerClose, onRequestDrawerOpen }) => event => {
      drawerOpen ? onRequestDrawerClose(event) : onRequestDrawerOpen(event);
    }
  })
)(Frame);