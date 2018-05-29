const isFn = prop => typeof prop === "function";

export default ({ children, render }, ...props) => {
  if (process.env.NODE_ENV !== 'production') {
    if(isFn(children) && isFn(render)) {
      console.warn(
        "You are using the children and render props together.\n" +
        "This is impossible, therefore, only the children will be used."
      );
    }
  }

  const fn = isFn(children) ? children : render;

  return fn ? fn(...props) : null;
};