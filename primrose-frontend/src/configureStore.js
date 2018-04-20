import { createStore, applyMiddleware, combineReducers, compose } from "redux";

import { connectRoutes } from "redux-first-router";

import routesMap from "./routesMap";
import options from "./options";
import { listener } from "./store/promiseListener";
import * as reducers from "./store";
import * as actionCreators from "./actions";

export default history => {
  const {
    reducer,
    middleware,
    enhancer
  } = connectRoutes(routesMap, options)

  const rootReducer = combineReducers({ ...reducers, location: reducer });
  const middlewares = applyMiddleware(middleware, listener.middleware);
  const enhancers = composeEnhancers(enhancer, middlewares);

  return createStore(rootReducer, enhancers);
}

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__
  ? window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__({ actionCreators })
  : compose;