import axios from "../axios";

const customersApiURL = "/customers";

export const create = customer =>
  axios.post(customersApiURL, {
    jsonrpc: "2.0",
    method: "create",
    params: { customer },
    id: Date.now(),
  });

export const deactivate = customerCodes =>
  axios.post(customersApiURL, {
    jsonrpc: "2.0",
    method: "delete",
    params: Array.isArray(customerCodes) ? {
      customerCodes
    } : {
        customerCode: customerCodes
      },
    id: Date.now(),
  });

export const update = (customer) =>
  axios.post(customersApiURL, {
    jsonrpc: "2.0",
    method: "update",
    params: {
      customer,
    },
    id: Date.now(),
  });

export const list = pagination =>
  axios.post(customersApiURL, {
    jsonrpc: "2.0",
    method: "list",
    params: {
      pagination,
    },
    id: Date.now(),
  });

export const view = customerCode =>
  axios.post(customersApiURL, {
    jsonrpc: "2.0",
    method: "get",
    params: {
      customerCode,
    },
    id: Date.now(),
  });