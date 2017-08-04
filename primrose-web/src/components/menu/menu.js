import React from "react";
import classnames from "classnames";

export default ({ children, className, ...rest }) => (
  <nav className={classnames({
    "menu": true,
  }, className)}
  {...rest}
  >
    {children}
  </nav>
);