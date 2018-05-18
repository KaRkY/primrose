export default loaders => (dispatch, getState, { action }) => {
  const state = getState();

  loaders.forEach(element => {
    if(element.canLoad(state, action)) {
      element.load(dispatch, element.getData(state, action));
    }
  });
}