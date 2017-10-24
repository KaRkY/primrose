import api, { operations } from "@/api";
import debounce from "lodash/debounce";

export default {
  loadAccounts: ({ commit, dispatch }, { page, size, sort }) => {
    const loading = debounce(l => commit("setLoading", l), 500);
    loading(true);
    api(
      operations.accounts,
      {
        page,
        size,
        sort,
      })
    .then((response) => {
      Object
        .keys(response.entities)
        .forEach((key) => {
          dispatch("entities/addAll", {
            name: key,
            data: response.entities[key],
          }, {
            root: true,
          });
        });
      commit("addResults", response.result);
      commit("setCount", response.count);
      loading(false);
    })
    .catch(error => console.log("lol", error));
  },
};
