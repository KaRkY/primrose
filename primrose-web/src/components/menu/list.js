import React from "react";
import classnames from "classnames";

export default ({ children, className, ...rest }) => (
  <ul className={classnames({
    "menu-list": true,
  }, className)}
  {...rest}
  >
    {children}
  </ul>
);