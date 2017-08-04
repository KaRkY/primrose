import axios from "axios";
import { normalize } from "normalizr";
import queryString from "query-string";
import Urls from "./urls";
import Schemas from "./schemas";

const call = (method, url, schema, params) => axios
  .request({
    method,
    url,
    transformResponse: [data => normalize(JSON.parse(data), schema)],
    paramsSerializer: params => queryString.stringify(params),
    params: params
  })
  .then(response => response)
  .catch(error => error);

  export default {
    call,

    getAccountsPage: ({page, size, query}) => call("get", Urls.accounts, Schemas.accounts, { page, size, query }),
  };