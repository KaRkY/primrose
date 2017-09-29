var fs = require("fs");

fs.readFile("./data.json", function(err, data) {
  var accounts = JSON.parse(data);

  var transformedAccounts = accounts.map(function(account) {
    return {
      accountType: account.type,
      displayName: account.isCompany ? account.company : account.firstName + " " + account.surname,
      name: account.isCompany ? account.company : account.firstName + " " + account.surname,
      email: (account.firstName + "." + account.surname + "@" + account.company.toLowerCase() + account.domain).toLowerCase(),
      phone: account.phone,
      website: (account.isCompany ? account.company + account.domain : account.firstName + "." + account.surname + account.domain).toLowerCase(),
      addresses: account.addresses.reduce(function(acc, address) {
        if(!acc[address.type]) {
          acc[address.type] = [];
        }
        acc[address.type].push({
          street: address.street,
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
          name: contact.firstName + " " + contact.surname,
          email: (contact.firstName + "." + contact.surname + "@" + account.company.toLowerCase() + account.domain).toLowerCase(),
          phone: contact.phone
        });

        return acc;
      }, {})
    };
  }, this);

  console.log("Transformed " + transformedAccounts.length + " accounts.");

  fs.writeFile("./transformed-data.json", JSON.stringify(transformedAccounts, null, " "), function (err) {
    console.error(err);
  });
});