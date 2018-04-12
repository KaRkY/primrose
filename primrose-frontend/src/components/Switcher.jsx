import React from "react";
//import { TransitionGroup, Transition } from "transition-group";
import universal from "react-universal-component";
import PageError from "./pages/PageError";
import PageLoading from "./pages/PageLoading";


const Switcher = props => (
  <UniversalComponent {...props} />
);

const UniversalComponent = universal(props => import(`./pages/${props.page}`), {
  minDelay: 500,
  loadingTransition: true,
  chunkName: props => props.page,
  loading: PageLoading,
  error: PageError,
});

export default Switcher;