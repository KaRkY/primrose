import {
  createStore,
  applyMiddleware,
  combineReducers,
  compose
} from "redux";

import {
  connectRoutes
} from "redux-first-router";
import { reducer as formReducer } from "redux-form";

import routesMap from "./routesMap";
import * as options from "./options";
import * as reducers from "./store";
import * as actionCreators from "./actions";

export default history => {
  const {
    reducer,
    middleware,
    enhancer
  } = connectRoutes(routesMap, {
    basename: options.basename,
    querySerializer: options.querySerializer,
  })

  const rootReducer = combineReducers({ ...reducers,
    location: reducer,
    form: formReducer,
  });
  const middlewares = applyMiddleware(middleware);
  const enhancers = composeEnhancers(enhancer, middlewares);

  return createStore(rootReducer, enhancers);
}

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ ?
  window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__({
    actionCreators
  }) :
  compose;