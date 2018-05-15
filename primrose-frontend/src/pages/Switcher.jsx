import React from "react";
//import { TransitionGroup, Transition } from "transition-group";
import universal from "react-universal-component";
import Error from "./Error";
import Loading from "../components/Loading";


const Switcher = props => (
  <UniversalComponent {...props} />
);

const UniversalComponent = universal(props => import(`../pages/${props.page}`), {
  minDelay: 500,
  loadingTransition: true,
  chunkName: props => props.page,
  loading: Loading,
  error: Error,
});

export default Switcher;