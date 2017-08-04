import React from "react";
import classnames from "classnames";

export default ({ children, className, shadow, ...rest }) => (
  <div className={classnames({
    "navbar": true,
    "has-shadow": shadow,
  }, className)}
    {...rest}
  >
    {children}
  </div>
);