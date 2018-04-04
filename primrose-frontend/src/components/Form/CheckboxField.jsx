import React from "react";
import { withStyles } from "material-ui/styles";
import Checkbox from "material-ui/Checkbox";
import { FormControl, FormHelperText, FormGroup, FormControlLabel } from "material-ui/Form";
import { Field } from "react-final-form";


export const style = theme => ({

});

const CheckboxField = props => (
  <Field {...props} type="checkbox">
    {({
      input: { checked, name, onChange, ...restInput },
      meta,
      label,
      ...rest
    }) => (
        <FormControlLabel
          control={
            <Checkbox
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

export default withStyles(style)(CheckboxField);