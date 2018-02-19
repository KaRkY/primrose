import React from "react";

const isEmptyChildren = children => React.Children.count(children) === 0;

const RenderComp = ({ children, component, render, ...props }) => {
  
  if (component) return React.createElement(component, props);

  if (render) return render(props);

  if (typeof children === "function") return children(props);

  if (children && !isEmptyChildren(children))
    return React.Children.only(children);

  return null;

};
export default RenderComp;