import { all, takeEvery, call, put, select, takeLatest } from "redux-saga/effects";
import AccountsActions from "./actions";
import AccountsSelectors from "./selectors";
import SettingsSelectors from "@/settings-selectors";
import LocationSelectors from "@/location-selectors";
import Api from "@/api";
import get from "lodash/get";
import isError from "lodash/isError";
import { redirect } from "redux-first-router";
import moment from "moment";

const processLoadAccountsPageDataRequest = function* (query) {
  yield put(AccountsActions.creators.requestLoadAccountsPage(query));
  const response = yield call(Api.getAccountsPage, query);
  yield put(AccountsActions.creators.respondLoadAccountsPage(response));
}

const processAccountSceneRequest = function* (action) {
  const lastUpdated = yield select(AccountsSelectors.search.lastUpdated);
  const query = yield action.meta.location.current.query || select(SettingsSelectors.accountsSearchDefaultPagging);

  if (!(lastUpdated && moment().subtract(1, "minute").isBefore(lastUpdated))) {
    yield processLoadAccountsPageDataRequest(query);
    yield put(AccountsActions.creators.requestAccountsScene(yield select(AccountsSelectors.search.query)));
  } else {
    if(!action.meta || !action.meta.location || !action.meta.location.current || !action.meta.location.current.query) {
      yield put(AccountsActions.creators.requestAccountsScene(yield select(AccountsSelectors.search.query)));
    }
  }
};

export default function* () {
  yield all([
    takeLatest(AccountsActions.types.ACCOUNTS_SCENE_REQUESTED, processAccountSceneRequest)
  ]);
};