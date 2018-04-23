import createCreateEntity from "../creators/createCreateEntity";
import createDeactivateEntity from "../creators/createDeactivateEntity";
import createEditEntity from "../creators/createEditEntity";
import createPagedEntity from "../creators/createPagedEntity";
import createEntity from "../creators/createEntity";
import { combineReducers } from "redux";
import { createSelector } from "reselect";

export default ({
  createAction,
  createFinishedAction,
  createErrorAction,
  editAction,
  editFinishedAction,
  editErrorAction,
  deactivateAction,
  deactivateFinishedAction,
  deactivateErrorAction,
  loadSingleAction,
  loadSingleFinishedAction,
  loadSingleErrorAction,
  loadPagedAction,
  loadPagedFinishedAction,
  loadPagedErrorAction,
  rootSelector,
}) => {
  const create = createCreateEntity({
    baseAction: createAction,
    createdAction: createFinishedAction,
    errorAction: createErrorAction,
    rootSelector: createSelector(rootSelector, root => root.create),
  });

  const edit = createEditEntity({
    baseAction: editAction,
    editedAction: editFinishedAction,
    errorAction: editErrorAction,
    rootSelector: createSelector(rootSelector, root => root.edit),
  });

  const deactivate = createDeactivateEntity({
    baseAction: deactivateAction,
    deactivatedAction: deactivateFinishedAction,
    errorAction: deactivateErrorAction,
    rootSelector: createSelector(rootSelector, root => root.deactivate),
  });

  const paged = createPagedEntity({
    loadingAction: loadPagedAction,
    fetchedAction: loadPagedFinishedAction,
    errorAction: loadPagedErrorAction,
    rootSelector: createSelector(rootSelector, root => root.paged),
  });

  const single = createEntity({
    loadingAction: loadSingleAction,
    fetchedAction: loadSingleFinishedAction,
    errorAction: loadSingleErrorAction,
    rootSelector: createSelector(rootSelector, root => root.single),
  });

  return {
    reducers: {
      create: combineReducers({ ...create.reducers }),
      edit: combineReducers({ ...edit.reducers }),
      deactivate: combineReducers({ ...deactivate.reducers }),
      paged: combineReducers({ ...paged.reducers }),
      single: combineReducers({ ...single.reducers }),
    },

    selectors: {
      create: create.selectors,
      edit: edit.selectors,
      deactivate: deactivate.selectors,
      paged: paged.selectors,
      single: single.selectors,
    },
  };
}