import * as actions from "../actions";
import getCurrentQuery from "../selectors/getCurrentQuery";

export default async (dispatch, getState, { action }) => {
  console.log(action);

  dispatch(actions.goToContacts({
    query: getCurrentQuery(getState()),
    selected: undefined,
  }));
};