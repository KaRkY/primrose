import React from "react";
import classnames from "classnames";

import MenuItem from "@material-ui/core/MenuItem";

import TextField from "../../fields/Text";
import { NestedField } from "react-form";

const CustomerPersonalInformation = ({ classes, className, types, relationTypes, field }) => (
  <NestedField field={field}>
    <div className={classnames(classes.root, className)}>
      <TextField field="fullName" label="Full name" />

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
  </NestedField>
);

export default CustomerPersonalInformation;