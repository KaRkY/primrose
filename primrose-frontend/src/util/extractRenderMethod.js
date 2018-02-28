import React from "react";
import isEmptyChildren from "./isEmptyChildren";

export default ({ children, component, render }) => {
  if (component) return (props) => React.createElement(component, props);

  if (render) return render;

  if (typeof children === "function") return children;

  if (children && !isEmptyChildren(children))
    return () => React.Children.only(children);

  return () => null;
};