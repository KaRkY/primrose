import React from "react";
import classnames from "classnames";

export default ({ children, className, ...rest }) => (
  <div className={classnames({
    "menu-label": true,
  }, className)}
  {...rest}
  >
    {children}
  </div>
);