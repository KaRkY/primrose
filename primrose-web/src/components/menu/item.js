import React from "react";
import classnames from "classnames";

export default ({ children, className, href, onClick, active, ...rest }) => (
  <li className={classnames({
    "menu-list": true,
    "is-active": active,
  }, className)}
    {...rest}
  >
    {children}
  </li>
);