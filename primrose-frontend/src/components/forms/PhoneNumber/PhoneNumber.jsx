import React from "react";
import classnames from "classnames";

import MenuItem from "@material-ui/core/MenuItem";

import TextField from "../../fields/Text";

const PhoneNumber = ({ classes, types, defaultType }) => (
  <div className={classes.root}>
    <TextField className={classnames(classes.type)} select name="type" label="Type">
      {Object.keys(types).map(key => (
        <MenuItem key={key} value={key}>{types[key]}</MenuItem>
      ))}
    </TextField>
    <TextField className={classnames(classes.margin, classes.text)} name="value" label="Phone number" />
  </div>
);

export default PhoneNumber;