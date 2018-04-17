import React from "react";
import { withStyles } from "material-ui/styles";
import Checkbox from "material-ui/Checkbox";
import { FormControlLabel } from "material-ui/Form";
import { Field } from "react-final-form";


export const style = theme => ({

});

const CheckboxField = props => (
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
        );
      } else {
        return (
          <Checkbox
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

export default withStyles(style)(CheckboxField);