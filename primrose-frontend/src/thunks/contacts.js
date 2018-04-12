import * as actions from "../actions";
import convertError from "../util/convertError";
import shouldReloadPageData from "../util/shouldReloadPageData";
import { load as loadContacts } from "../api/contacts";
import * as location from "../store/location";
import * as contacts from "../store/contacts";


export const load = async (dispatch, getState, { action }) => {
  const state = getState();
  const pagination = location.getCurrentPagination(state);

  if (shouldReloadPageData(getState, action, contacts.isLoading)) {
    dispatch(actions.contactsLoading());
    loadContacts(pagination)
      .then(result => dispatch(actions.contactsFetched(result.data)))
      .catch(error => dispatch(actions.contactsError(convertError(error))));
  }
};

export const del = async (dispatch, getState, {
  action
}) => {
  console.log(action);

  dispatch(actions.contacts({
    query: location.getCurrentQuery(getState()),
    selected: undefined,
  }));
};