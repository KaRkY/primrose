import React from "react";
import { connect } from "react-redux";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";
import { TransitionGroup, Transition } from "transition-group";
import universal from "react-universal-component";
import isLoading from "../selectors/isLoading";

const style = theme => ({
  container: {
    display: "flex",
    flexWrap: "wrap",
  },
  formControl: {
    margin: theme.spacing.unit,
  },
});

const mapState = ({ page, direction, ...state }) => ({
  page,
  direction,
  isLoading: isLoading(state)
});

const enhance = compose(
  connect(mapState),
  withStyles(style),
);


const Switcher = ({ page, direction, isLoading }) => (
  <UniversalComponent page={page} isLoading={isLoading} />
);

const UniversalComponent = universal(props => import(`./pages/${props.page}`), {
  minDelay: 500,
  loadingTransition : false,
  chunkName: props => props.page,
  loading: () => <span>Loading</span>,
  error: () => <span>PAGE NOT FOUND - 404</span>
});

export default enhance(Switcher);