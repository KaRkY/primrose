import axios from "../axios";
import * as actions from "../actions";
import convertError from "../util/convertError";

const apiUrl = "/meta";

const create = entity => props => {
  const { dispatch, state, getData, getError, } = props;

  const { entityName, entityLoadAction, entityFinishedAction, entityErrorAction, } = entity;

  if (!getData(state) || getError(state)) {
    dispatch(entityLoadAction());
    return axios.post(apiUrl, {
      jsonrpc: "2.0",
      method: entityName,
      id: Date.now(),
    })
      .then(result => dispatch(entityFinishedAction(result.data.result)))
      .catch(error => dispatch(entityErrorAction(convertError(error))));
  } else {
    return Promise.resolve();
  }
};

export const customerRelationTypes = create({
  entityName: "customerRelation",
  entityLoadAction: actions.customerRelationTypesLoad,
  entityFinishedAction: actions.customerRelationTypesLoadFinished,
  entityErrorAction: actions.customerRelationTypesLoadError,
});

export const customerTypes = create({
  entityName: "customer",
  entityLoadAction: actions.customerTypesLoad,
  entityFinishedAction: actions.customerTypesLoadFinished,
  entityErrorAction: actions.customerTypesLoadError,
});

export const emailTypes = create({
  entityName: "email",
  entityLoadAction: actions.emailTypesLoad,
  entityFinishedAction: actions.emailTypesLoadFinished,
  entityErrorAction: actions.emailTypesLoadError,
});

export const phoneNumberTypes = create({
  entityName: "phoneNumber",
  entityLoadAction: actions.phoneNumberTypesLoad,
  entityFinishedAction: actions.phoneNumberTypesLoadFinished,
  entityErrorAction: actions.phoneNumberTypesLoadError,
});