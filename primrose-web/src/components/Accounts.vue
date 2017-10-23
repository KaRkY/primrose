<template>
  <pre class="accounts">
    {{resp}}
  </pre>
</template>

<script>
import axios from "axios";
import api, { operations } from "@/api";

export default {
  name: "Accounts",
  data: () => ({
    resp: "",
  }),

  created() {
    this.resp = "Dela";
    this.fetchData();
  },

  watch: {
    $route: "fetchData",
  },

  methods: {
    fetchData() {
      axios({
        method: "post",
        url: "/login",
        data: {
          username: "root",
          password: "root",
        },
      }).then((respo) => {
        api(
          operations.accounts,
          {
            page: 1,
            size: 25,
          },
          respo.headers.authorization)
        .then((response) => {
          this.resp = JSON.stringify(response.data, null, 2);
        });
      });
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>