import axios from "axios";

export default {
  authorize: ({ commit, dispatch }, { username, password }) => {
    if (window.localStorage.token) {
      commit("setAuthorized", true);
      return Promise.resolve();
    }

    return axios.post("/login", {
      username,
      password,
    })
    .then((response) => {
      window.localStorage.token = response.headers.authorization;
      commit("setAuthorized", true);
    });
  },
};
