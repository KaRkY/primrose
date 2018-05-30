import React from "react";
import { Field } from "react-form";

import MUICheckbox from "@material-ui/core/Checkbox";
import FormControlLabel from "@material-ui/core/FormControlLabel";

const Checkbox = ({ validate, field, onChange, onBlur, label, ...props }) => (
  <Field validate={validate} field={field}>{
    ({ value, setValue, setTouched }) => {
      const checkboxControl = (
        <MUICheckbox
          checked={!!value}
          onChange={(e, checked) => {
            setValue(checked);
            if (onChange) {
              onChange(e, checked);
            }
          }}
          onBlur={e => {
            setTouched();
            if (onBlur) {
              onBlur(e);
            }
          }}
          {...props}
        />
      );
      if (label) {
        return (
          <FormControlLabel
            control={checkboxControl}
            label={label}
          />
        );
      } else {
        return checkboxControl;
      }
    }
  }</Field>
);

export default Checkbox;