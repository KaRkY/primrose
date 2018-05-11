import axios from "../axios";
import * as actions from "../actions";
import convertError from "../util/convertError";

const apiUrl = "/meta";

const create = entity => () => axios.post(apiUrl, {
  jsonrpc: "2.0",
  method: entity,
  id: Date.now(),
});

export const customerRelationTypes = create("customerRelation",);

export const customerTypes = create("customer",);

export const emailTypes = create("email",);

export const phoneNumberTypes = create("phoneNumber",);