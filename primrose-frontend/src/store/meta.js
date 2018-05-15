import { combineReducers } from "redux";
import * as actions from "../actions";
import createMetaEntity from "./creators/createMetaEntity";

const customerRelationTypes = createMetaEntity({
  loadingAction: actions.customerRelationTypesLoad,
  fetchedAction: actions.customerRelationTypesFinished,
  errorAction: actions.customerRelationTypesError,
  rootSelector: state => state.meta.customerRelationTypes,
});

const customerTypes = createMetaEntity({
  loadingAction: actions.customerTypesLoad,
  fetchedAction: actions.customerTypesFinished,
  errorAction: actions.customerTypesError,
  rootSelector: state => state.meta.customerTypes,
});

const emailTypes = createMetaEntity({
  loadingAction: actions.emailTypesLoad,
  fetchedAction: actions.emailTypesFinished,
  errorAction: actions.emailTypesError,
  rootSelector: state => state.meta.emailTypes,
});

const phoneNumberTypes = createMetaEntity({
  loadingAction: actions.phoneNumberTypesLoad,
  fetchedAction: actions.phoneNumberTypesFinished,
  errorAction: actions.phoneNumberTypesError,
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