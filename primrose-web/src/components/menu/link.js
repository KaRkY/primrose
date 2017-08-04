import React from "react";
import classnames from "classnames";

export default ({ children, className, ...rest }) => (
  <a className={classnames(className)}
    {...rest}
  >
    {children}
  </a>
);