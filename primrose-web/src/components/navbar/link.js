import React from "react";
import classnames from "classnames";

export default ({ children, className, active, ...rest }) => (
  <a className={classnames({
    "navbar-link": true,
    "is-active": active,
  }, className)}
    {...rest}
  >
    {children}
  </a>
);