import axios from "axios";

export default axios.create({
  baseURL: "http://localhost:9080/graphql/",
  timeout: 5000,
});