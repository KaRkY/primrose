import { axios } from "./general";

export const load = (query = { page: 0, size: 5 }) => {
  return axios.get("/customers", {
    params: query
  });
};

export const create = customer => {
  return axios.post("/customers", customer);
};

export const del = id => {
  return axios.delete(`/customers/${id}`);
};