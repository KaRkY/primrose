import axios from "axios";
import { normalize } from "normalizr";

export { default as operations } from "./operations";

export default (operation, variables, authorization) => axios
  .request({
    method: "post",
    url: "/graphql",
    transformResponse: [
      data => normalize(JSON.parse(data).data[operation.field], operation.schema),
    ],
    data: {
      query: operation.query,
      variables,
    },
    headers: {
      authorization,
    },
  })
  .then(response => response)
  .catch(error => error);
