import React from "react";
import { Field } from "react-final-form";

import FormControlLabel from "@material-ui/core/FormControlLabel";
import Switch from "@material-ui/core/Switch";
import withStyles from "@material-ui/core/styles/withStyles";


export const style = theme => ({

});

const SwitchField = props => (
  <Field {...props} type="checkbox">{
    ({
      input: { checked, name, onChange, ...restInput },
      meta,
      label,
      ...rest
    }) => {
      if (label) {
        return (
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
        );
      } else {
        return (
          <Switch
            name={name}
            inputProps={restInput}
            onChange={onChange}
            checked={!!checked}
            {...rest}
          />
        );
      }
    }
  }</Field>
);

export default withStyles(style)(SwitchField);