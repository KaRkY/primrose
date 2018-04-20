import { combineReducers } from "redux";
import * as actions from "../actions";
import createMetaEntity from "./creators/createMetaEntity";

const customerRelationTypes = createMetaEntity({
  loadingAction: actions.customerRelationTypesLoad,
  fetchedAction: actions.customerRelationTypesLoadFinished,
  errorAction: actions.customerRelationTypesLoadError,
  rootSelector: state => state.meta.customerRelationTypes,
});

const customerTypes = createMetaEntity({
  loadingAction: actions.customerTypesLoad,
  fetchedAction: actions.customerTypesLoadFinished,
  errorAction: actions.customerTypesLoadError,
  rootSelector: state => state.meta.customerTypes,
});

const emailTypes = createMetaEntity({
  loadingAction: actions.emailTypesLoad,
  fetchedAction: actions.emailTypesLoadFinished,
  errorAction: actions.emailTypesLoadError,
  rootSelector: state => state.meta.emailTypes,
});

const phoneNumberTypes = createMetaEntity({
  loadingAction: actions.phoneNumberTypesLoad,
  fetchedAction: actions.phoneNumberTypesLoadFinished,
  errorAction: actions.phoneNumberTypesLoadError,
  rootSelector: state => state.meta.phoneNumberTypes,
});

export const reducer = combineReducers({
  customerRelationTypes: combineReducers(customerRelationTypes.reducers),
  customerTypes: combineReducers(customerTypes.reducers),
  emailTypes: combineReducers(emailTypes.reducers),
  phoneNumberTypes: combineReducers(phoneNumberTypes.reducers),
});

export default {
  customerRelationTypes: customerRelationTypes.selectors,
  customerTypes: customerTypes.selectors,
  emailTypes: emailTypes.selectors,
  phoneNumberTypes: phoneNumberTypes.selectors,
};