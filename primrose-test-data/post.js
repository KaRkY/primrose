var fs = require("fs");
var request = require("request");
var async = require("async");

var addressesQueue = async.queue(function (task, done) {
  request({
    uri: "http://localhost:9080/accounts/" + task.accountId + "/addresses/" + task.address.type,
    method: "POST",
    headers: {
      authorization: task.authorization
    },
    json: {
      street: task.address.street,
      streetNumber: task.address.streetNumber,
      city: task.address.city,
      postalCode: task.address.postalCode,
      state: task.address.state,
      country: task.address.country
    }
  }, function (error, response, body) {
    if (error) return done(error);
    if (response.statusCode != 200) return done(response.statusCode);
    
    console.log("Imported address: " + JSON.stringify(body));
    
    done();
  });
});

var contactsQueue = async.queue(function (task, done) {
  request({
    uri: "http://localhost:9080/accounts/" + task.accountId + "/contacts/" + task.contact.type,
    method: "POST",
    headers: {
      authorization: task.authorization
    },
    json: {
      name: task.contact.firstName + " " + task.contact.surname,
      email: (task.contact.firstName + "." + task.contact.surname + "@" + task.account.company.toLowerCase() + task.account.domain).toLowerCase(),
      phone: task.contact.phone
    }
  }, function (error, response, body) {
    if (error) return done(error);
    if (response.statusCode != 200) return done(response.statusCode);
    
    console.log("Imported contact: " + JSON.stringify(body));
    
    done();
  });
});

var accountQueue = async.queue(function (task, done) {
  request({
    uri: "http://localhost:9080/accounts",
    method: "POST",
    headers: {
      authorization: task.authorization
    },
    json: {
      type: task.account.type,
      displayName: task.account.isCompany ? task.account.company : task.account.firstName + " " + task.account.surname,
      name: task.account.isCompany ? task.account.company : task.account.firstName + " " + task.account.surname,
      email: (task.account.firstName + "." + task.account.surname + "@" + task.account.company.toLowerCase() + task.account.domain).toLowerCase(),
      phone: task.account.phone,
      website: (task.account.isCompany ? task.account.company + task.account.domain : task.account.firstName + "." + task.account.surname + task.account.domain).toLowerCase()
    }
  }, function (error, response, body) {
    if (error) return done(error);
    if (response.statusCode != 200) return done(response.statusCode);
    
    task.account.addresses.forEach(function (address) {
      addressesQueue.push({
        authorization: task.authorization,
        account: task.account,
        accountId: body.id,
        address: address
      });
    });
    
    task.account.contacts.forEach(function (contact) {
      contactsQueue.push({
        authorization: task.authorization,
        account: task.account,
        accountId: body.id,
        contact: contact
      });
    });
    console.log("Imported account: " + JSON.stringify(body));
    
    done();
  });
});

fs.readFile("./data.json", function(err, data) {
  var accounts = JSON.parse(data);
  
  request({
    uri: "http://localhost:9080/login",
    method: "POST",
    json: {
      username: "user",
      password: "user"
    }
  }, function (error, response, body) {
    if (error) {
      console.error("Could not login: " + error);
      process.exit(1);
    }
    if (response.statusCode != 200) {
      console.error("Could not login: " + response.statusCode);
      process.exit(1);
    }
    
    accounts.forEach(function (account) {
      accountQueue.push({
        authorization: response.headers["authorization"],
        account: account
      });
    });
  });
});