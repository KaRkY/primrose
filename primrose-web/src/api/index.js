import axios from "axios";

export { default as operations } from "./operations";

export default (operation, variables) => axios
  .request({
    method: "post",
    url: "/graphql",
    data: {
      query: operation.query,
      variables,
    },
    headers: {
      authorization: window.localStorage.token,
    },
  })
  .then(response => operation.transform(response.data));
