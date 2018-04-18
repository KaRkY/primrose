import {
  combineReducers
} from "redux";
import * as actions from "../actions";
import createMetaEntity from "./creators/createMetaEntity";

const customerRelationTypes = createMetaEntity({
  loadingAction: actions.customerRelationTypesLoad,
  fetchedAction: actions.customerRelationTypesLoadFinished,
  errorAction: actions.customerRelationTypesLoadError,
  rootSelector: state => state.meta.customerRelationTypes,
  apiUrl: "/meta",
  apiEntity: "customerRelation",
});

const customerTypes = createMetaEntity({
  loadingAction: actions.customerTypesLoad,
  fetchedAction: actions.customerTypesLoadFinished,
  errorAction: actions.customerTypesLoadError,
  rootSelector: state => state.meta.customerTypes,
  apiUrl: "/meta",
  apiEntity: "customer",
});

const emailTypes = createMetaEntity({
  loadingAction: actions.emailTypesLoad,
  fetchedAction: actions.emailTypesLoadFinished,
  errorAction: actions.emailTypesLoadError,
  rootSelector: state => state.meta.emailTypes,
  apiUrl: "/meta",
  apiEntity: "email",
});

const phoneNumberTypes = createMetaEntity({
  loadingAction: actions.phoneNumberTypesLoad,
  fetchedAction: actions.phoneNumberTypesLoadFinished,
  errorAction: actions.phoneNumberTypesLoadError,
  rootSelector: state => state.meta.phoneNumberTypes,
  apiUrl: "/meta",
  apiEntity: "phoneNumber",
});

const { reducer: customerRelationTypesReducer, ...customerRelationTypesRest } = customerRelationTypes;
const { reducer: customerTypesReducer, ...customerTypesRest } = customerTypes;
const { reducer: emailTypesReducer, ...emailTypesRest } = emailTypes;
const { reducer: phoneNumberTypesReducer, ...phoneNumberTypesRest } = phoneNumberTypes;


export default {
  reducer: combineReducers({
    customerRelationTypes: customerRelationTypesReducer,
    customerTypes: customerTypesReducer,
    emailTypes: emailTypesReducer,
    phoneNumberTypes: phoneNumberTypesReducer,
  }),

  customerRelationTypes: customerRelationTypesRest,
  customerTypes: customerTypesRest,
  emailTypes: emailTypesRest,
  phoneNumberTypes: phoneNumberTypesRest,
};