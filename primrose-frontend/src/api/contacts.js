import { axios } from "./general";

export const load = (query = { page: 0, size: 5 }) => {
  return axios
    .get("/contacts", {
      params: query,
    });
};