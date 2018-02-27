import React from "react";
import { Machine } from "xstate";
import axios from "axios";
import isEqual from "lodash/isEqual";
import omit from "lodash/omit";

const loadingMachine = Machine({
  initial: "idle",
  states: {
    idle: {
      on: { load: "loading" }
    },
    loading: {
      on: {
        error: "error",
        success: "success",
        cancel: "idle",
      }
    },
    error: {
      on: { load: "loading" }
    },
    success: {
      on: { load: "loading" }
    },
  }
});

const isEmptyChildren = children => React.Children.count(children) === 0;

class DataLoading extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      networkState: loadingMachine.initialState,
      cancelToken: null,
      response: null,
      error: null,
    };

    this.cancel = this.cancel.bind(this);
    this.load = this.load.bind(this);
    this.success = this.success.bind(this);
    this.error = this.error.bind(this);
    this.fetch = this.fetch.bind(this);
  }

  componentDidMount() {
    this.fetch();
  }

  shouldComponentUpdate(prevProps, nextState) {
    const omitedProps = ["render", "component", "children"];
    return !isEqual(omit(prevProps, omitedProps), omit(this.props, omitedProps))
    || !isEqual(this.state, nextState);
  }

  componentDidUpdate(prevProps) {
    const omitedProps = ["render", "component", "children"];
    if(!isEqual(omit(prevProps, omitedProps), omit(this.props, omitedProps))) {
      this.fetch();
    }
  }

  cancel() {
    this.state.cancelToken.cancel();
    this.setState(state => ({
      ...state,
      networkState: loadingMachine.transition(state.networkState.value, "cancel"),
      cancelToken: null,
    }));
  }

  load() {
    const cancelToken = axios.CancelToken.source();
    this.setState(state => ({
      ...state,
      networkState: loadingMachine.transition(state.networkState.value, "load"),
      cancelToken,
    }));

    return cancelToken;
  }

  success(response) {
    this.setState(state => ({
      ...state,
      networkState: loadingMachine.transition(state.networkState.value, "success"),
      response,
      cancelToken: null,
    }));
  }

  error(error) {
    this.setState(state => ({
      ...state,
      networkState: loadingMachine.transition(state.networkState.value, "error"),
      error,
      cancelToken: null,
    }));
  }

  fetch() {
    const { children, component, render, ...props } = this.props;
    if (this.state.cancelToken) {
      this.cancel();
    }

    const cancelToken = this.load();

    axios
      .request({
        ...props,
        cancelToken: cancelToken.token,
      })
      .then(this.success)
      .catch(this.error);
  }

  render() {
    const { children, component, render } = this.props;

    const props = {
      networkState: this.state.networkState.value,
      response: this.state.response,
      error: this.state.error,
    };

    if (component) return React.createElement(component, props);

    if (render) return render(props);

    if (typeof children === "function") return children(props);

    if (children && !isEmptyChildren(children))
      return React.Children.only(children);

    return null;
  }
}

export default DataLoading;