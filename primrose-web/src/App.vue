<template>
  <v-app id="primrose" dark>
    <v-navigation-drawer persistent v-model="drawer" enable-resize-watcher app>
      <v-toolbar flat>
        <v-toolbar-title>Primrose</v-toolbar-title>
      </v-toolbar>
      <v-list>
          <v-list-tile :to="{ name: 'Dashboard' }" exact>
            <v-list-tile-title class="title">Dashboard</v-list-tile-title>
          </v-list-tile>

          <v-list-tile :to="{ name: 'Accounts' }">
            <v-list-tile-title class="title">Accounts</v-list-tile-title>
          </v-list-tile>
        </v-list>
    </v-navigation-drawer>
    <v-toolbar color="orange" dark fixed app>
      <v-toolbar-side-icon @click.stop="drawer = !drawer"></v-toolbar-side-icon>
      <v-toolbar-title>{{ $route.meta.title }}</v-toolbar-title>
    </v-toolbar>
    <main>
      <v-content>
        <router-view></router-view>
      </v-content>
    </main>
    <v-footer fixed app>
    </v-footer>

    <v-snackbar
      :timeout="6000"
      bottom
      v-model="snackbar"
    >
      <v-icon v-if="current.icon" dark>{{ current.icon }}</v-icon>
      <v-spacer></v-spacer>
      <div>{{ current.text }}</div>
      <v-btn v-if="current.action" flat color="pink" @click.native="dispatch">{{ current.action.text }}</v-btn>
    </v-snackbar>
  </v-app>
</template>

<script>
export default {
  name: "app",
  data: () => ({
    drawer: true,
  }),

  computed: {
    snackbar: {
      get() {
        return this.$store.getters["notifications/show"];
      },
      set(show) {
        this.$store.dispatch("notifications/show", show);
      },
    },
    current() {
      return this.$store.getters["notifications/current"];
    },
  },

  methods: {
    dispatch() {
      this.$store.dispatch("notifications/dispatch");
    },
  },
};
</script>

<style lang="stylus">
@import '~vuetify/src/stylus/main.styl';
</style>
