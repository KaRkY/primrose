import React from "react";
import renderProp from "../../util/renderProp";

import { Form } from "react-final-form";

import Button from "@material-ui/core/Button";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogTitle from "@material-ui/core/DialogTitle";

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
            <DialogContent>{renderProp(rest, props)}</DialogContent>
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