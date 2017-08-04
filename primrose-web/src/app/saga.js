import { all } from "redux-saga/effects";

import accounts from "./accounts/saga";

// single entry point to start all Sagas at once
export default function* () {
  yield all([
    accounts()
  ]);
}