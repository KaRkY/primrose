import React from "react";
import classnames from "classnames";

export default ({ children, className, ...rest }) => (
  <div className={classnames({
    "navbar-burger": true,
  }, className)}
    {...rest}
  >
    <span />
    <span />
    <span />
  </div>
);