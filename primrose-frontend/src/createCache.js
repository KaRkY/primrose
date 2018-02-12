export default () => {
  const cache = {};

  return {
    get: (location) => {
      const { key } = location;
      console.log("cache get", location);
      return cache[key];
    },
    set: response => {
      const { key } = response.location;
      console.log("cache set", response);
      cache[key] = response;
    }
  };
}