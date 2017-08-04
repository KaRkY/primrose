import merge from "lodash/merge";

export default (state = {}, action) => {
  if(action.payload && action.payload.entities) {
    return merge({}, state, action.payload.entities);
  } else {
    return state;
  }
}