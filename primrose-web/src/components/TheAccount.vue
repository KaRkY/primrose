<template>
  <v-container fluid grid-list-lg>
    <v-layout row wrap>
      <v-flex xs6>
        <v-text-field
          label="Display name"
          v-model="account.displayName"
          :disabled="!edit"
        ></v-text-field>
        <v-text-field
          label="Name"
          v-model="account.name"
          :disabled="!edit"
        ></v-text-field>
        <v-text-field
          label="Email"
          v-model="account.email"
          :disabled="!edit"
        ></v-text-field>
      </v-flex>
      <v-flex xs6>
        <v-text-field
          label="Phone"
          v-model="account.phone"
          :disabled="!edit"
        ></v-text-field>
        <v-text-field
          label="Website"
          v-model="account.website"
          :disabled="!edit"
        ></v-text-field>
      </v-flex>
      <v-flex xs12>
        <v-text-field
          label="Description"
          v-model="account.description"
          multi-line
          :disabled="!edit"
        ></v-text-field>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import debounce from "lodash/debounce";

export default {
  name: "TheAccount",
  props: ["accountId"],
  data: () => ({
    edit: true,
    account: {
      displayName: null,
      name: null,
      email: null,
      phone: null,
      website: null,
      description: null,
    },
  }),

  created() {
    this.$store.dispatch("account/load", this.accountId);
    Object.assign(this.account, this.$store.getters["account/account"]);
  },

  watch: {
    account: {
      handler(to) {
        this.commitAccount(to);
      },
      deep: true,
    },
  },

  methods: {
    commitAccount: debounce(function (account) {
      this.$store.commit("account/account", account);
    }, 500),
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
