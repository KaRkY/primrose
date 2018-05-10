import React from "react";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";
import { NOT_FOUND } from "redux-first-router";
import isError from "lodash/isError";

import Typography from "material-ui/Typography";

const contentStyle = theme => ({
  root: {
    position: "absolute",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    whiteSpace: "nowrap",
  },
});

const enhance = compose(
  withStyles(contentStyle)
);

const Content = props => {
  if (props.pageType === NOT_FOUND) {
    return (
      <div className={props.classes.root}>
        <Typography variant="display2">Ups 404 <span role="img" aria-label="Surprised Face">😯</span></Typography>
      </div>
    );
  }

  if (isError(props.error)) {
    console.log(props);
    return (
      <div className={props.classes.root}>
        <Typography variant="display2">Oh no <span role="img" aria-label="Surprised Face">😯</span></Typography>
        <Typography variant="caption">Could not load page!</Typography>
      </div>
    );
  }

  if (props.error && props.error.timeout) {
    return (
      <div className={props.classes.root}>
        <Typography variant="display2">Oh no <span role="img" aria-label="Surprised Face">😯</span></Typography>
        <Typography variant="caption">Data request timeout!</Typography>
      </div>
    );
  }

  if (props.error && props.error.response) {
    return (
      <div className={props.classes.root}>
        <Typography variant="display2">Oh no <span role="img" aria-label="Surprised Face">😯</span></Typography>
        <Typography variant="caption">Something went wrong with API response!</Typography>
      </div>
    );
  }

  if (props.error) {
    return (
      <div className={props.classes.root}>
        <Typography variant="display2">Oh no API {props.error.status}<span role="img" aria-label="Surprised Face">😯</span></Typography>
        <Typography variant="caption">Something went wrong with API response!</Typography>
      </div>
    );
  }

  return (
    <div className={props.classes.root}>
      <Typography variant="display2">Oh no <span role="img" aria-label="Surprised Face">😯</span></Typography>
      <Typography variant="caption">Something went wrong!</Typography>
    </div>
  );
};

export default enhance(Content);