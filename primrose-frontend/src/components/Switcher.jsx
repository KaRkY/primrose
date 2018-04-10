import React from "react";
import { connect } from "react-redux";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";
//import { TransitionGroup, Transition } from "transition-group";
import universal from "react-universal-component";
import isLoading from "../selectors/isLoading";
import PageNotFound from "./pages/PageNotFound";
import PageLoading from "./pages/PageLoading";

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
  loadingTransition : true,
  chunkName: props => props.page,
  loading: () => <PageLoading />,
  error: () => <PageNotFound />
});

export default enhance(Switcher);