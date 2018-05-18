import * as actions from "../actions";
import * as location from "../store/location";
import * as customersApi from "../api/customers";

import reduxifyApi from "../util/reduxifyApi";
import shouldReloadPageData from "../util/shouldReloadPageData";

export const list = reduxifyApi({
  load: actions.customerListLoad, 
  finished: actions.customerListFinished, 
  error: actions.customerListError,
  api: customersApi.list,
  extractor: result => result.data.result,
  reload: (state, action) => shouldReloadPageData(state, action),
  data: state => location.getCurrentPagination(state),
});

export const view = reduxifyApi({
  load: actions.customerViewLoad, 
  finished: actions.customerViewFinished, 
  error: actions.customerViewError,
  api: customersApi.view,
  extractor: result => result.data.result,
  data: state => location.getCurrentData(state).customer,
});

export const update = reduxifyApi({
  load: actions.customerUpdateLoad, 
  finished: actions.customerUpdateFinished, 
  error: actions.customerUpdateError,
  api: customersApi.view,
  extractor: result => result.data.result,
  data: state => location.getCurrentData(state).customer,
});