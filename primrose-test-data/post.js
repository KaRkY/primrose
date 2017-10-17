var fs = require("fs");
var request = require("request");

fs.readFile("./data.json", function(err, data) {
  var accounts = JSON.parse(data);
  
  var transformedAccounts = accounts.map(function(account) {
    return {
      type: account.type,
      displayName: account.isCompany ? account.company : account.firstName + " " + account.surname,
      name: account.isCompany ? account.company : account.firstName + " " + account.surname,
      email: (account.firstName + "." + account.surname + "@" + account.company.toLowerCase() + account.domain).toLowerCase(),
      phone: account.phone,
      website: (account.isCompany ? account.company + account.domain : account.firstName + "." + account.surname + account.domain).toLowerCase(),
      addresses: account.addresses.map(function(address) {
        return {
          type: address.type,
          street: address.street,
          streetNumber: address.streetNumber,
          city: address.city,
          postalCode: address.postalCode,
          state: address.state,
          country: address.country
        };
      }),
      contacts: account.contacts.map(function(contact) {
        return {
          type: contact.type,
          name: contact.firstName + " " + contact.surname,
          email: (contact.firstName + "." + contact.surname + "@" + account.company.toLowerCase() + account.domain).toLowerCase(),
          phone: contact.phone
        };
      })
    };
  }, this);
  
  request({
    uri: "http://localhost:9080/login",
    method: "POST",
    json: {
      username: "root",
      password: "root"
    }
  }, function (error, response, body) {
    if (!error && response.statusCode == 200) {
      request({
        uri: "http://localhost:9080/graphql",
        method: "POST",
        headers: {
          authorization: response.headers["authorization"]
        },
        json: {
          query: "mutation import($accounts: [CreateAccount]!){importAccounts(accounts: $accounts){id}}",
          variables: {
            accounts: transformedAccounts
          }
        }
      }, function (error, response, body) {
        if (!error && response.statusCode == 200) {
          console.log("Transfere comlete") // Print the shortened url.
          console.log(body);
        } else {
          console.error(error);
          console.error(body);
        }
      });
    } else {
      console.error(error);
    }
  });
});