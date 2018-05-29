import React from "react";

import CircularProgress from "@material-ui/core/CircularProgress";

class Loading extends React.Component {

  constructor(props) {
    super(props);
    this.setTimeout = this.setTimeout.bind(this);
  }

  state = {
    loading: true,
  }

  setTimeout() {
    this.setState(state => ({ ...state, loading: true}));
    this.currentTimeout = setTimeout(() => {
      this.currentTimeout = null;
      this.setState(state => ({ ...state, loading: false}));
    }, 5000);
  }

  componentDidMount() {
    this.setTimeout();
  }

  componentWillUnmount() {
    clearTimeout(this.currentTimeout);
  }

  componentDidUpdate(prevProps, prevState) {
    if(prevProps.loading === false && this.props.loading === true) {
      if(this.currentTimeout) {
        clearTimeout(this.currentTimeout);
      }
      this.setTimeout();
    }
  }

  render() {
    const { classes, loading, children } = this.props;
    return (
      <div className={classes.root}>
        {children}
        { (loading || this.state.loading) &&
          <div className={classes.loading}>
            <div className={classes.icon}>
              <CircularProgress />
            </div>
          </div>
        }
      </div>
    );
  }
}

export default Loading;