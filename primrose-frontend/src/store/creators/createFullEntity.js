import createCreateEntity from "../creators/createCreateEntity";
import createDeleteEntity from "../creators/createDeleteEntity";
import createEditEntity from "../creators/createEditEntity";
import createPagedEntity from "../creators/createPagedEntity";
import createEntity from "../creators/createEntity";
import { combineReducers } from "redux";
import {
  createSelector
} from "reselect";

export default ({
  entityName,
  createAction,
  createFinishedAction,
  createErrorAction,
  createApiParameters,
  editAction,
  editFinishedAction,
  editErrorAction,
  editApiParameters,
  deleteAction,
  deleteFinishedAction,
  deleteErrorAction,
  deleteApiParameters,
  loadSingleAction,
  loadSingleFinishedAction,
  loadSingleErrorAction,
  loadSingleApiParameters,
  loadPagedAction,
  loadPagedFinishedAction,
  loadPagedErrorAction,
  loadPagedApiParameters,
  rootSelector,
  apiUrl,
}) => {
  const create = createCreateEntity({
    baseAction: createAction,
    createdAction: createFinishedAction,
    errorAction: createErrorAction,
    rootSelector: createSelector(rootSelector, root => root.create),
    apiUrl,
    apiParameters: createApiParameters,
  });

  const edit = createEditEntity({
    baseAction: editAction,
    editedAction: editFinishedAction,
    errorAction: editErrorAction,
    rootSelector: createSelector(rootSelector, root => root.edit),
    apiUrl,
    apiParameters: editApiParameters
  });

  const del = createDeleteEntity({
    baseAction: deleteAction,
    createdAction: deleteFinishedAction,
    errorAction: deleteErrorAction,
    rootSelector: createSelector(rootSelector, root => root.delete),
    apiUrl,
    apiParameters: deleteApiParameters
  });

  const paged = createPagedEntity({
    loadingAction: loadPagedAction,
    fetchedAction: loadPagedFinishedAction,
    errorAction: loadPagedErrorAction,
    rootSelector: createSelector(rootSelector, root => root.paged),
    apiUrl,
    apiParameters: loadPagedApiParameters
  });

  const single = createEntity({
    loadingAction: loadSingleAction,
    fetchedAction: loadSingleFinishedAction,
    errorAction: loadSingleErrorAction,
    rootSelector: createSelector(rootSelector, root => root.single),
    apiUrl,
    apiParameters: loadSingleApiParameters
  });

  const { reducer: createReducer, ...createRest } = create;
  const { reducer: editReducer, ...editRest } = edit;
  const { reducer: deleteReducer, ...deleteRest } = del;
  const { reducer: pagedReducer, ...pagedRest } = paged;
  const { reducer: singleReducer, ...singleRest } = single;

  return {
    reducer: combineReducers({
      create: createReducer,
      edit: editReducer,
      delete: deleteReducer,
      paged: pagedReducer,
      single: singleReducer,
    }),

    create: createRest,
    edit: editRest,
    delete: deleteRest,
    paged: pagedRest,
    single: singleRest,
  };
}