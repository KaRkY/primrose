import isArray from "lodash/isArray";

export default obj => isArray(obj) ? obj : (obj ? [obj] : undefined);