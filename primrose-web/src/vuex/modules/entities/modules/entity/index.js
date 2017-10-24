import Vue from "vue";

export default {
  namespaced: true,

  state: () => {},

  mutations: {
    add: (state, { id, data }) => {
      Vue.set(state, id, data);
    },

    addAll: (state, data) => {
      Object
        .keys(data)
        .forEach(id => Vue.set(state, id, data[id]));
    },

    remove: (state, { id }) => {
      Vue.remove(state, id);
    },
  },

  getters: {
    get: state => id => state[id],
  },
};
