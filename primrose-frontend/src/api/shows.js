import axios from "axios";

export const loadShow = (id) => {
  return axios.get(`http://api.tvmaze.com/shows/${id}`);
};