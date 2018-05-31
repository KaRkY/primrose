import React from "react";
import { FieldArray, FormSection } from "redux-form";

import Typography from "@material-ui/core/Typography";
import IconButton from "@material-ui/core/IconButton";

import AddIcon from "@material-ui/icons/Add";
import RemoveIcon from "@material-ui/icons/Remove";

const renderSubFields = ({ classes, label, children, fields, meta, ...rest }) => (
  <div className={classes.root}>
    <Typography variant="title">{label}</Typography>
    <div className={classes.elements}>
      {fields.map((name, index) => {
        const clonedElement = React.cloneElement(children);
        return (
          <FormSection key={index} name={name}>
            <div key={index} className={classes.element}>
              {clonedElement}
              <IconButton onClick={() => fields.remove(index)}>
                <RemoveIcon />
              </IconButton>
            </div>
          </FormSection>
        );
      })}
    </div>
    <div>
      <IconButton onClick={() => fields.push({})}>
        <AddIcon />
      </IconButton>
    </div>
  </div>
);

const Array = props => (
  <FieldArray {...props} component={renderSubFields} />
);

export default Array;