var path = require("path");

module.exports = {
  type: "react-app",
  webpack:{
    aliases: {
      "@": path.resolve("src")
    }
  },
  devServer: {
    proxy: {
      "/api": {
        target: "http://localhost:9080",
        pathRewrite: { "^/api": "" }
      }
    }
  }
}
