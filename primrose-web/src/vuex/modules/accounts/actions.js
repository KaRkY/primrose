import api, { operations } from "@/api";
import debounce from "lodash/debounce";

export default {
  load: ({ commit, dispatch }, query) => {
    const loading = debounce(l => commit("loading", l), 500);
    loading(true);
    api(operations.accounts, query)
      .then((response) => {
        Object
          .keys(response.entities)
          .forEach((key) => {
            commit(`entities/${key}/addAll`, response.entities[key], {
              root: true,
            });
          });
        commit("addResults", response.result);
        commit("count", response.count);
        loading(false);
      });
  },
};
