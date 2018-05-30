import React from "react";
import classnames from "classnames";

import MenuItem from "@material-ui/core/MenuItem";
import Typography from "@material-ui/core/Typography";

import TextField from "../../fields/Text";
import { NestedField } from "react-form";

const ContactInformation = ({ classes, className, types, relationTypes }) => (
  <div className={classnames(classes.root, className)}>
    <Typography variant="title">Contact information</Typography>

    <TextField field="displayName" label="Display name" />

    <div className={classes.types}>
      <TextField select field="type" label="Customer type">
        {Object.keys(types).map(key => (
          <MenuItem key={key} value={key}>{types[key]}</MenuItem>
        ))}
      </TextField>

      <TextField select field="relationType" label="Customer relation type">
        {Object.keys(relationTypes).map(key => (
          <MenuItem key={key} value={key}>{relationTypes[key]}</MenuItem>
        ))}
      </TextField>
    </div>

    <TextField
      multiline
      rows={5}
      rowsMax={5}
      field="description"
      label="Description"
    />
  </div>
);

export default ContactInformation;