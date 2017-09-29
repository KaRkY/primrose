var fs = require("fs");
var request = require("request");

fs.readFile("./data.json", function(err, data) {
  var accounts = JSON.parse(data);
  
  var transformedAccounts = accounts.map(function(account) {
    return {
      accountType: account.type,
      displayName: account.isCompany ? account.company : account.firstName + " " + account.surname,
      name: account.isCompany ? account.company + " " + account.index : account.firstName + " " + account.surname + " " + account.index,
      email: (account.firstName + "." + account.surname + "@" + account.company.toLowerCase() + account.domain).toLowerCase(),
      phone: account.phone,
      website: (account.isCompany ? account.company + account.domain : account.firstName + "." + account.surname + account.domain).toLowerCase(),
      addresses: account.addresses.reduce(function(acc, address) {
        if(!acc[address.type]) {
          acc[address.type] = [];
        }
        acc[address.type].push({
          street: address.street,
          streetNumber: address.streetNumber,
          city: address.city,
          postalCode: address.postalCode,
          state: address.state,
          country: address.country
        });

        return acc;
      }, {}),
      contacts: account.contacts.reduce(function(acc, contact) {
        if(!acc[contact.type]) {
          acc[contact.type] = [];
        }
        acc[contact.type].push({
          name: contact.firstName + " " + contact.surname + " " + account.index,
          email: (contact.firstName + "." + contact.surname + " " + account.index + "@" + account.company.toLowerCase() + account.domain).toLowerCase(),
          phone: contact.phone
        });

        return acc;
      }, {})
    };
  }, this);
  
  request({
    uri: "http://localhost:9080/login",
    method: "POST",
    json: {
      username: "user",
      password: "user"
    }
  }, function (error, response, body) {
    if (!error && response.statusCode == 200) {
      request({
        uri: "http://localhost:9080/import",
        method: "POST",
        headers: {
          authorization: response.headers["authorization"]
        },
        json: transformedAccounts
      }, function (error, response, body) {
        if (!error && response.statusCode == 200) {
          console.log("Transfere comlete") // Print the shortened url.
        } else {
          console.error(response.statusCode);
          console.error(body);
        }
      });
    } else {
      console.error(error);
    }
  });
});