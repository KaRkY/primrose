import React from "react";
import { NestedField } from "react-form";

import Typography from "@material-ui/core/Typography";
import Button from "@material-ui/core/Button";
import IconButton from "@material-ui/core/IconButton";

import AddIcon from "@material-ui/icons/Add";
import RemoveIcon from "@material-ui/icons/Remove";

const Array = ({ classes, validate, field, onChange, label, children, ...props }) => (
  <NestedField field={field} defaultValues={[]}>
    {fieldApi => console.log(fieldApi) || (
      <div className={classes.root}>
        <Typography variant="title">{label}</Typography>
        <div className={classes.elements}>
          {fieldApi.values && fieldApi.values.map((value, index) => {
            const clonedElement = React.cloneElement(children, { field: index });
            return (
              <div key={index} className={classes.element}>
                {clonedElement}
                <IconButton onClick={() => fieldApi.removeValue(undefined, index)}>
                  <RemoveIcon />
                </IconButton>
              </div>
            );
          })}
        </div>
        <div>
          <IconButton onClick={() => fieldApi.addValue(undefined, undefined)}>
            <AddIcon />
          </IconButton>
        </div>
      </div>
    )}
  </NestedField>
);

export default Array;