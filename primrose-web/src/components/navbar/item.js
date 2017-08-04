import React from "react";
import classnames from "classnames";

export default ({ children, className, onClick, href, tab, hasDropdown, active, ...rest }) => {
  let Tag = (onClick || href) ? "a" : "div";
  return (
    <Tag className={classnames({
      "navbar-item": true,
      "has-dropdown": hasDropdown,
      "is-tab": tab,
      "is-active": active,
    }, className)}
      onClick={onClick}
      href={href}
      {...rest}
    >
      {children}
    </Tag>
  );
};