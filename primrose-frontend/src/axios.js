import ax from "axios";
import * as options from "./options";

const axios = ax.create({
  paramsSerializer: options.querySerializer.stringify,
  timeout: options.apiTimeout,
  baseURL: options.apiBasename,
});

export default axios;