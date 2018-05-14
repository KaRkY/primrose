import React from "react";

import compose from "recompose/compose";

import Grid from "@material-ui/core/Grid";
import MenuItem from "@material-ui/core/MenuItem";
import withStyles from "@material-ui/core/styles/withStyles";

import TextField from "../TextField";

export const style = theme => ({

});

const enhance = compose(
  withStyles(style),
);

const EmailForm = ({
  classes,
  types,
  handleSubmit
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
          label="Email"
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

export default enhance(EmailForm);