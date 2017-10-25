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
      <v-toolbar-title>Application</v-toolbar-title>
    </v-toolbar>
    <main>
      <v-content>
        <router-view></router-view>
      </v-content>
    </main>
    <v-footer fixed app>
    </v-footer>
  </v-app>
</template>

<script>
export default {
  name: "app",
  data: () => ({
    drawer: true,
  }),
};
</script>

<style lang="stylus">
@import '~vuetify/src/stylus/main.styl';
</style>
