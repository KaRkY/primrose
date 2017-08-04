import React from "react";
import classnames from "classnames";

export default ({ children, className, ...rest }) => (
  <div className={classnames({
    "navbar-divider": true
  }, className)}
    {...rest}
  >
    {children}
  </div>
);