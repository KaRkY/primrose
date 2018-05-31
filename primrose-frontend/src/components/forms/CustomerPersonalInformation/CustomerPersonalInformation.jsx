import React from "react";
import classnames from "classnames";

import MenuItem from "@material-ui/core/MenuItem";
import Typography from "@material-ui/core/Typography";

import TextField from "../../fields/Text";
import CheckboxField from "../../fields/Checkbox";

const CustomerPersonalInformation = ({ classes, className, types, relationTypes, name }) => (
  <div className={classnames(classes.root, className)}>
    <Typography variant="title">Personal information</Typography>

    <TextField name="fullName" label="Full name" />

    <TextField name="displayName" label="Display name" />

    <CheckboxField name="test" label="Test name" />

    <div className={classes.types}>
      <TextField select name="type" label="Customer type">
        {Object.keys(types).map(key => (
          <MenuItem key={key} value={key}>{types[key]}</MenuItem>
        ))}
      </TextField>

      <TextField select name="relationType" label="Customer relation type">
        {Object.keys(relationTypes).map(key => (
          <MenuItem key={key} value={key}>{relationTypes[key]}</MenuItem>
        ))}
      </TextField>
    </div>

    <TextField
      multiline
      rows={5}
      rowsMax={5}
      name="description"
      label="Description"
    />
  </div>
);

export default CustomerPersonalInformation;