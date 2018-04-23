import React from "react";
import extractRenderMethod from "../../util/extractRenderMethod";

import { Form } from "react-final-form";
import Dialog, { DialogActions, DialogContent, DialogTitle } from "material-ui/Dialog";
import Button from "material-ui/Button";

const DialogForm = ({
  classes,
  title,
  okButtonText = "Add",
  cancelButtonText = "Cancel",
  onSubmit,
  open,
  onOpen,
  onClose,
  ...rest
}) => (
    <Form
      onSubmit={(values, form) => onSubmit(values, {
        close: () => {
          onClose();
          form.reset();
        },
        form,
      })}
      {...rest}
      render={props => (
          <Dialog onClose={() => {
            onClose();
            props.form.reset();
          }} open={open}>
            <DialogTitle>{title}</DialogTitle>
            <DialogContent>{extractRenderMethod(rest)(props)}</DialogContent>
            <DialogActions>
              <Button
                onClick={() => {
                  onClose();
                  props.form.reset();
                }}
              >{cancelButtonText}</Button>
              <Button onClick={props.handleSubmit} color="primary">{okButtonText}</Button>
            </DialogActions>
          </Dialog>
        )} />
  );

export default DialogForm;