import React from "react";
import { Field } from "react-final-form";

import Checkbox from "@material-ui/core/Checkbox";
import FormControlLabel from "@material-ui/core/FormControlLabel";

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

export default CheckboxField;