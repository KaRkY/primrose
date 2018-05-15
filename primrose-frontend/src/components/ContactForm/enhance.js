import withStateHandlers from "recompose/withStateHandlers";

export default withStateHandlers(
  { emailInitialValue: {}, emailDialogOpen: false, phoneInitialValue: {}, phoneDialogOpen: false },
  {
    onOpenEmailForm: (state) => event => ({ ...state, emailDialogOpen: true }),
    onCloseEmailForm: (state) => event => ({ ...state, emailDialogOpen: false }),
    onOpenPhoneForm: (state) => event => ({ ...state, phoneDialogOpen: true }),
    onClosePhoneForm: (state) => event => ({ ...state, phoneDialogOpen: false }),
    setEmailInitialValue: state => value => ({ ...state, emailInitialValue: value }),
    setPhoneInitialValue: state => value => ({ ...state, phoneInitialValue: value }),
  });