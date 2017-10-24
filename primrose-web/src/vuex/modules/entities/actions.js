export default {
  addAll: ({ commit }, { name, data }) => {
    commit(`${name}/addAll`, data);
  },
};
