import React from "react";

export default (WrapperComponent, wrapperProps) => BaseComponent => ({ children, ...rest }) => {
  let wprops;
  if (typeof wrapperProps === "function") {
    wprops = wrapperProps(rest);
  } else {
    wprops = wrapperProps;
  }

  return (
    <WrapperComponent {...wprops}>
      <BaseComponent {...restProps}>
        {children}
      </BaseComponent>
    </WrapperComponent>
  );
};