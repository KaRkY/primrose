import * as actions from "../actions";
import * as location from "../store/location";
import * as contactsApi from "../api/contacts";

import reduxifyApi from "../util/reduxifyApi";
import shouldReloadPageData from "../util/shouldReloadPageData";

export const list = reduxifyApi({
  load: actions.contactListLoad, 
  finished: actions.contactListFinished, 
  error: actions.contactListError,
  api: contactsApi.list,
  extractor: result => result.data.result,
  reload: (state, action) => shouldReloadPageData(state, action),
  data: state => location.getCurrentPagination(state),
});

export const view = reduxifyApi({
  load: actions.contactViewLoad, 
  finished: actions.contactViewFinished, 
  error: actions.contactViewError,
  api: contactsApi.view,
  extractor: result => result.data.result,
  data: state => location.getCurrentData(state).contact,
});

export const update = reduxifyApi({
  load: actions.contactUpdateLoad, 
  finished: actions.contactUpdateFinished, 
  error: actions.contactUpdateError,
  api: contactsApi.view,
  extractor: result => result.data.result,
  data: state => location.getCurrentData(state).contact,
});