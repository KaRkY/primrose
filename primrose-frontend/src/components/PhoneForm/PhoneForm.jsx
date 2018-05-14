import React from "react";

import Grid from "@material-ui/core/Grid";
import MenuItem from "@material-ui/core/MenuItem";

import TextField from "../TextField";

const PhoneForm = ({
  classes,
  types,
  handleSubmit,
}) => (
    <Grid container spacing={16}>
      <Grid item xs={12}>
        <TextField select name="type" label="Type" fullWidth>
          {Object.keys(types).map(key => (
            <MenuItem key={key} value={key}>{types[key]}</MenuItem>
          ))}
        </TextField>
      </Grid>
      <Grid item xs={12}>
        <TextField
          name="value"
          label="Phone"
          fullWidth
          onKeyPress={event => {
            if (event.key === "Enter") {
              event.preventDefault();
              handleSubmit();
            }
          }} />
      </Grid>
    </Grid>

  );

export default PhoneForm;