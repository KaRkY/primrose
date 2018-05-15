import axios from "../axios";

const apiUrl = "/contacts";

export const create = contact =>
  axios.post(apiUrl, {
    jsonrpc: "2.0",
    method: "create",
    params: {
      contact,
    },
    id: Date.now(),
  });

export const deactivate = contactCodes => axios.post(apiUrl, {
  jsonrpc: "2.0",
  method: "delete",
  params: Array.isArray(contactCodes) ? {
    contactCodes
  } : {
      contactCode: contactCodes
    },
  id: Date.now(),
});

export const update = contact => axios.post(apiUrl, {
  jsonrpc: "2.0",
  method: "update",
  params: {
    contact,
  },
  id: Date.now(),
});

export const list = pagination => axios.post(apiUrl, {
  jsonrpc: "2.0",
  method: "list",
  params: {
    pagination,
  },
  id: Date.now(),
});

export const view = contactCode => axios.post(apiUrl, {
  jsonrpc: "2.0",
  method: "get",
  params: {
    contactCode,
  },
  id: Date.now(),
});