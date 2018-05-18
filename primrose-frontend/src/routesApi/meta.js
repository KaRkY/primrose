import * as actions from "../actions";
import meta from "../store/meta";
import * as metaApi from "../api/meta";

import reduxifyApi from "../util/reduxifyApi";

export const customerTypes = reduxifyApi({
  load: actions.customerTypesLoad, 
  finished: actions.customerTypesFinished, 
  error: actions.customerTypesError,
  api: metaApi.customerTypes,
  extractor: result => result.data.result,
  reload: (state) => !meta.customerTypes.getData(state) || meta.customerTypes.getError(state),
});

export const customerRelationTypes = reduxifyApi({
  load: actions.customerRelationTypesLoad, 
  finished: actions.customerRelationTypesFinished, 
  error: actions.customerRelationTypesError,
  api: metaApi.customerRelationTypes,
  extractor: result => result.data.result,
  reload: (state) => !meta.customerRelationTypes.getData(state) || meta.customerRelationTypes.getError(state),
});

export const emailTypes = reduxifyApi({
  load: actions.emailTypesLoad, 
  finished: actions.emailTypesFinished, 
  error: actions.emailTypesError,
  api: metaApi.emailTypes,
  extractor: result => result.data.result,
  reload: (state) => !meta.emailTypes.getData(state) || meta.emailTypes.getError(state),
});

export const phoneNumberTypes = reduxifyApi({
  load: actions.phoneNumberTypesLoad, 
  finished: actions.phoneNumberTypesFinished, 
  error: actions.phoneNumberTypesError,
  api: metaApi.phoneNumberTypes,
  extractor: result => result.data.result,
  reload: (state) => !meta.phoneNumberTypes.getData(state) || meta.phoneNumberTypes.getError(state),
});