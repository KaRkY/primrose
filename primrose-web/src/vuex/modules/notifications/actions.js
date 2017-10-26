

export default {
  offer: ({ commit, getters }, notification) => {
    if (getters.head) {
      commit("offer", notification);
    } else {
      commit("offer", notification);
      commit("current", notification);
    }
    commit("show", true);
  },

  show: ({ commit, getters }, show) => {
    commit("show", show);
    if (!show) {
      commit("remove");
      commit("current", {});
      if (getters.head) {
        setTimeout(() => {
          commit("current", getters.head);
          commit("show", true);
        }, 750);
      }
    }
  },

  dispatch: ({ dispatch, getters }) => {
    const action = getters.current.action;
    dispatch("show", false);
    dispatch(
      action.command,
      action.parameter, {
        root: true,
      });
  },
};
