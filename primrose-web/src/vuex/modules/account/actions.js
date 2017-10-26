import api, { operations } from "@/api";
import debounce from "lodash/debounce";

export default {
  load: ({ commit, dispatch, rootState }, accountId) => {
    if (rootState.entities.account[accountId]) {
      commit("account", rootState.entities.account[accountId]);
    } else {
      const loading = debounce(l => commit("loading", l), 500);
      loading(true);
      api(operations.account, {
        accountId,
      })
        .then((response) => {
          Object
            .keys(response.entities)
            .forEach((key) => {
              commit(`entities/${key}/addAll`, response.entities[key], {
                root: true,
              });
            });
          commit("account", response.entities.account[response.result]);
          loading(false);
        })
        .catch(() => {
          loading(false);
          dispatch(
            "notifications/offer", {
              icon: "error",
              text: "Account loading failed.",
              action: {
                text: "Retry",
                command: "account/load",
                parameter: accountId,
              },
            }, {
              root: true,
            });
        });
    }
  },
};
