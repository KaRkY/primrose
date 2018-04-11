import React from "react";
//import { TransitionGroup, Transition } from "transition-group";
import universal from "react-universal-component";
import PageNotFound from "./pages/PageNotFound";
import PageLoading from "./pages/PageLoading";


const Switcher = props => (
  <UniversalComponent {...props} />
);

const UniversalComponent = universal(props => import(`./pages/${props.page}`), {
  minDelay: 500,
  loadingTransition: true,
  chunkName: props => props.page,
  loading: PageLoading,
  error: PageNotFound,
});

export default Switcher;