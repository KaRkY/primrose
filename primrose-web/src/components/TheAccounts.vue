<template>
  <v-container fluid fill-height>
    <v-layout>
      <v-flex xs12>
        <v-card>
          <v-card-title>
            Accounts
            <v-spacer></v-spacer>
            <v-text-field
              append-icon="search"
              label="Search"
              single-line
              hide-details
            ></v-text-field>
          </v-card-title>
          <v-data-table
            v-bind:headers="headers"
            v-bind:items="items"
            v-bind:pagination.sync="pagination"
            :total-items="totalItems"
            :loading="loading"
          >
            <template slot="items" slot-scope="props">
              <tr>
                <td><router-link :to="{ name: 'Account', params: { accountId: props.item.id }}">{{ props.item.displayName }}</router-link></td>
                <td class="text-xs-right">{{ props.item.type }}</td>
                <td class="text-xs-right"><a :href="'mailto://' + props.item.email">{{ props.item.email }}</a></td>
                <td class="text-xs-right">{{ props.item.phone }}</td>
                <td class="text-xs-right"><a :href="'http://' + props.item.website">{{ props.item.website }}</a></td>
              </tr>
            </template>
          </v-data-table>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import { toQuery, fromQuery } from "@/util/pagination";

export default {
  name: "TheAccounts",
  props: ["query"],
  data: () => ({
    headers: [
      { text: "Display name", align: "left", value: "displayName" },
      { text: "Type", value: "type" },
      { text: "Email", value: "email" },
      { text: "Phone", value: "phone" },
      { text: "Website", value: "website" },
    ],
    pagination: {},
  }),

  created() {
    Object.assign(this.pagination, fromQuery(this.query));
    this.$store.dispatch("accounts/load", toQuery(this.pagination));
  },

  watch: {
    pagination(to) {
      this.$router.push({
        name: "Accounts",
        query: toQuery(to),
      });
    },
    query(to) {
      this.$store.dispatch("accounts/load", to);
    },
  },

  computed: {
    items() {
      return this.$store.getters["accounts/results"];
    },
    totalItems() {
      return this.$store.getters["accounts/count"];
    },
    loading() {
      return this.$store.getters["accounts/loading"] && "orange";
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>