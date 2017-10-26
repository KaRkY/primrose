import Vue from "vue";

export default {
  offer: (state, notification) => {
    state.notifications.push(notification);
  },
  remove: (state) => {
    state.notifications.shift();
  },
  current: (state, notification) => {
    Vue.set(state, "current", notification);
  },
  show: (state, show) => {
    Vue.set(state, "show", show);
  },
};
