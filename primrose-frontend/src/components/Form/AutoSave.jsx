import React from "react";
import { FormSpy } from "react-final-form";

class AutoSave extends React.Component {
  constructor(props) {
    super(props);
    this.state = { values: props.values, submitting: false };
  }

  componentWillReceiveProps(nextProps) {
    if (this.props.active && this.props.active !== nextProps.active) {
      // blur occurred
      this.save(this.props.active);
    }
  }

  save = async blurredField => {
    if (this.promise) {
      await this.promise;
    }
    const { values, setFieldData, save } = this.props;


    this.setState({ submitting: true, values });
    setFieldData(blurredField, { saving: true });
    this.promise = save(values);
    await this.promise;
    delete this.promise;
    this.setState({ submitting: false });
    setFieldData(blurredField, { saving: false });
  }

  render() {
    // This component doesn"t have to render anything, but it can render
    // submitting state.
    return null;
  }
}

// Make a HOC
// This is not the only way to accomplish auto-save, but it does let us:
// - Use built-in React lifecycle methods to listen for changes
// - Maintain state of when we are submitting
// - Render a message when submitting
// - Pass in save prop nicely
export default props => (
  <FormSpy
    {...props}
    subscription={{ active: true, values: true }}
    component={AutoSave}
  />
);