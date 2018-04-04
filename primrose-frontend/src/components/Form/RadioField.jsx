import React from "react";
import { withStyles } from "material-ui/styles";
import Radio from "material-ui/Radio";
import { FormControl, FormHelperText, FormGroup, FormControlLabel } from "material-ui/Form";
import { Field } from "react-final-form";


export const style = theme => ({

});

const RadioField = props => (
  <Field {...props} type="radio">
    {({
      input: { checked, name, onChange, ...restInput },
      meta,
      label,
      ...rest
    }) => (
        <FormControlLabel
          control={
            <Radio
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

export default withStyles(style)(RadioField);