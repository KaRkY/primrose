import React from "react";
import classnames from "classnames";

import MenuItem from "@material-ui/core/MenuItem";

import TextField from "../../fields/Text";
import { NestedField } from "react-form";

const PhoneNumber = ({ classes, types, defaultType, field }) => (
  <NestedField field={field} defaultValues={{ type: defaultType }}>
    <div className={classes.root}>
      <TextField className={classnames(classes.type)} select field="type" label="Type">
        {Object.keys(types).map(key => (
          <MenuItem key={key} value={key}>{types[key]}</MenuItem>
        ))}
      </TextField>
      <TextField className={classnames(classes.margin, classes.text)} field="phoneNumber" label="Phone number" />
    </div>
  </NestedField>
);

export default PhoneNumber;