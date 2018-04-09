import React from "react";
import mapKeys from "lodash/mapKeys";

const extractProps = ({ children, ...rest }) => rest;

const convertToProps = (component) => {
  if (!component.type.config) {
    console.error(`Unknown child element ${component.type.displayName}.`);
    return;
  }

  const result = {
    props: extractProps(component.props, component.type.config),
    config: component.type.config,
    component: component,
  };

  const assign = (value) => {
    if(result.config.ignoreProps) {
      result.props = value;
    } else {
      result.props["children"] = value;
    }
  };

  if (!result.config.renderChildren) {
    const child = React.Children.map(component.props.children, convertToProps);
    if (child) {
      if (component.type.config.ordered) {
        assign(child.map(c => c.props));
      } else {
        assign(child.reduce(
          (acc, c) => Object.assign(acc, { [c.component.type.propName]: c.props }), 
          {}));
      }
    }
  } else {
    assign(component.props.children);
  }

  if (result.config.mapProps) {
    result.props = mapKeys(result.props, (value, key) => result.config.mapProps[key] || key);
  }

  return result;
}

const extractFromChildren = (children, ordered) => {
  const propsChildren = React.Children.map(children, convertToProps) || [];
  return propsChildren.reduce((acc, child) => {
    if(ordered) {
      acc.push(child.props);
    } else {
      acc[child.component.type.propName] = child.props;
    }
    return acc;
  }, ordered ? [] : {});
};

const generateComponents = config => {
  return Object.keys(config)
    .reduce((acc, key) => {
      acc[key] = () => null;
      acc[key].propTypes = config[key].propTypes;
      acc[key].displayName = key;
      acc[key].config = config[key];
      acc[key].propName = config[key].key || key;

      return acc;
    }, {});
};

export default config => Component => {

  const DynamicComponent = ({ children, ...rest }) => {
    const props = {};
    if(config.propName) {
      props[config.propName] = extractFromChildren(children, config.ordered);
    } else {
      Object.assign(props, extractFromChildren(children, config.ordered));
    }

    return <Component {...props} {...rest} />;
  };

  if (config) {
    Object.assign(DynamicComponent, generateComponents(config));
  }
  return DynamicComponent;
};