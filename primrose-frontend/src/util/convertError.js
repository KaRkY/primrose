export default error => {
  if(error.code === "ECONNABORTED") {
    return {
      timeout: true,
    };
  }

  if(error.response) {
    return {
      data: error.response.data,
      status: error.response.status,
    };
  }

  if(error.request) {
    return {
      response: true,
    };
  }

  return {};
}