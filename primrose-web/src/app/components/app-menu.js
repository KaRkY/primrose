import React, { Component } from "react";
import classnames from "classnames";

import { Menu, MenuItem, MenuLabel, MenuList, MenuLink } from "@/components/menu";

const renderMenuList = items => (
  <MenuList>{
    items.map(item => (
      <MenuItem key={item.name}>
        <MenuLink>{item.name}</MenuLink>
        {item.children && renderMenuList(item.schildren)}
      </MenuItem>
    ))
  }</MenuList>
);

const AppMenu = ({ items }) => (
  <Menu>{
    items.map(item => [
      item.label && <MenuLabel key={item.label}>{item.label}</MenuLabel>,
      renderMenuList(items.children)
    ])
  }</Menu>
);

export default AppMenu;