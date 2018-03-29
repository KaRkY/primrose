import React from "react";

const extractProps = ({ children, ...rest }) => rest;

const convertToProps = (component) => {
  if(!component.type.config) {
    console.error(`Unknown child element ${component.type.displayName}.`);
    return;
  }

  const result = {
    props: extractProps(component.props),
    config: component.type.config,
    component: component,
  };
  const child = React.Children.map(component.props.children, convertToProps);
  if(child) {
    if(component.type.config.ordered) {
      result.props["children"] = child.map(c => c.props);
    } else {
      result.props["children"] = child.reduce((acc, c) => {
        acc[c.component.type.propName] = c.props;
        return acc;
      }, {});
    }
  }

  return result;
}

const extractFromChildren = children => {
  const result = {};
  React.Children.forEach(children, child => {
    const r = convertToProps(child);
    result[r.component.type.propName] = r.props;
  });

  return result;
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

  const DynamicComponent = ({children, ...rest}) => <Component {...extractFromChildren(children)} />;

  if (config) {
    Object.assign(DynamicComponent, generateComponents(config));
  }
  return DynamicComponent;
};