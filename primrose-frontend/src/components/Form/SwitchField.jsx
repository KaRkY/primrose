import React from "react";
import { withStyles } from "material-ui/styles";
import Switch from "material-ui/Switch";
import { FormControlLabel } from "material-ui/Form";
import { Field } from "react-final-form";


export const style = theme => ({

});

const SwitchField = props => (
  <Field {...props} type="checkbox">
    {({
      input: { checked, name, onChange, ...restInput },
      meta,
      label,
      ...rest
    }) => (
        <FormControlLabel
          control={
            <Switch
              name={name}
              inputProps={restInput}
              onChange={onChange}
              checked={!!checked}
              {...rest}
            />
          }
          label={label}
        />
      )}
  </Field>
);

export default withStyles(style)(SwitchField);