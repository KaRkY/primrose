import React from "react";
import { Field } from "react-final-form";

import FormControlLabel from "@material-ui/core/FormControlLabel";
import Radio from "@material-ui/core/Radio";
import withStyles from "@material-ui/core/styles/withStyles";


export const style = theme => ({

});

const RadioField = props => (
  <Field {...props} type="radio">{
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
        );
      } else {
        return (
          <Radio
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

export default withStyles(style)(RadioField);