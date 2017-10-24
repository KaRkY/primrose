<template>
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
            <tr @click="selectItem(props.item.id)">
              <td>{{ props.item.displayName }}</td>
              <td class="text-xs-right">{{ props.item.type }}</td>
              <td class="text-xs-right">{{ props.item.email }}</td>
              <td class="text-xs-right">{{ props.item.phone }}</td>
              <td class="text-xs-right">{{ props.item.website }}</td>
            </tr>
          </template>
        </v-data-table>
      </v-card>
    </v-flex>
  </v-layout>
</template>

<script>
import { toQuery, fromQuery } from "@/util/pagination";

export default {
  name: "Accounts",
  data: () => ({
    pagination: {},
    headers: [
      { text: "Display name", align: "left", value: "displayName" },
      { text: "Type", value: "type" },
      { text: "Email", value: "email" },
      { text: "Phone", value: "phone" },
      { text: "Website", value: "website" },
    ],
  }),

  mounted() {
    // this.fetchData();
  },

  watch: {
    $route(to) {
      this.pagination = fromQuery(to.query);
    },
    pagination: {
      handler() {
        this.fetchData();
        const route = {
          name: "Accounts",
          query: toQuery(this.pagination),
        };
        if (this.$router.currentRoute.query.page && this.$router.currentRoute.query.size) {
          this.$router.push(route);
        } else {
          this.$router.replace(route);
        }
      },
      deep: true,
    },
  },

  computed: {
    items() {
      return this.$store.getters["accounts/getResults"];
    },
    totalItems() {
      return this.$store.getters["accounts/getCount"];
    },
    loading() {
      return this.$store.getters["accounts/isLoading"] && "orange";
    },
  },

  methods: {
    fetchData() {
      this.$store.dispatch("accounts/loadAccounts", {
        page: this.pagination.page,
        size: this.pagination.rowsPerPage,
        sort: `${this.pagination.descending ? "-" : ""}${this.pagination.sortBy}`,
      });
    },
    selectItem() {

    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>