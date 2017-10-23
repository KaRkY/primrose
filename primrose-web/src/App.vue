<template>
  <v-app 
    id="primrose" 
    dark
  >
    <v-navigation-drawer 
      persistent
      v-model="drawer"
      enable-resize-watcher
      app
    >
      <v-toolbar 
        flat
      >
        <v-list>
          <v-list-tile>
            <v-list-tile-title 
              class="title"
            >
              Primrose
            </v-list-tile-title>
          </v-list-tile>
        </v-list>
      </v-toolbar>
    </v-navigation-drawer>
    <v-toolbar 
      color="orange" 
      dark
      fixed
      app
    >
      <v-toolbar-side-icon @click.stop="drawer = !drawer"></v-toolbar-side-icon>
      <v-toolbar-title>Application</v-toolbar-title>
    </v-toolbar>
    <main>
      <v-content>
        <v-container 
          fluid
        >
          <router-view></router-view>
        </v-container>
      </v-content>
    </main>
    <v-footer 
      fixed
      app
    >
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
