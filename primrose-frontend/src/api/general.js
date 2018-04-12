import range from "lodash/range";
import ax from "axios";
import queryString from "query-string";
import MockAdapter from "axios-mock-adapter";

const relationTypes = ["Customer", "Partner", "Investor", "Reseller"];
const types = ["Person", "Company"];

const generateArray = (count, single) => {
  return range(count)
    .map(single);
};

const generateContact = index => {
  return {
    id: index,
    name: `Contact ${index}`,
    primaryEmail: `Contact.${index}@email.com`,
    primaryPhone: "040404040",
  };
}

const generateCustomer = index => {
  return {
    id: index,
    relationType: relationTypes[index % 4],
    type: types[index % 2],
    fullName: `Customer ${index}`,
    dindexsplayName: `Customer ${index}`,
  };
}
const customerCount = 24;
const customers = generateArray(customerCount, generateCustomer);
const contactCount = 24;
const contacts = generateArray(contactCount, generateContact);

const hasPagination = query => query && query.page !== undefined && query.size !== undefined;
const isError = percent => (Math.floor(Math.random() * 100) + 1) < percent;
const isTimeout = percent => (Math.floor(Math.random() * 100) + 1) < percent;
const isServerError = percent => (Math.floor(Math.random() * 100) + 1) < percent;


export const axios = ax.create({
  paramsSerializer: queryString.stringify,
  timeout: 2000,
});
const mock = new MockAdapter(axios);

const respond = ({
  config,  
  result, 
  errorProbability, 
  timeoutProbability,
  serverErrorProbability,
  delay,
  onSuccess,
}) => {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        if (isError(errorProbability)) {
          if(isTimeout(timeoutProbability)) {
            const error = new Error("timeout of " + config.timeout + "ms exceeded");
            error.config = config;
            error.code = "ECONNABORTED";
            return reject(error);
          } else {
            if(isServerError(serverErrorProbability)) {
              resolve([500, { error: true }]);
            } else {
              reject(new Error("Some network error"));
            }
          }
        } else {
          resolve([200, onSuccess ? onSuccess(config.data && JSON.parse(config.data)) : result]);
        }
      }, delay);
    });
}

mock.onGet("/customers").reply(config => {
  const startIndex = hasPagination(config.params) ? config.params.size * config.params.page : 0;
  const endIndex = hasPagination(config.params) ? startIndex + config.params.size : customers.length;
  return respond({
    config,
    result: { data: customers.slice(startIndex, endIndex), count: customers.length},
    errorProbability: 0,
    timeoutProbability: 50,
    serverErrorProbability: 90,
    delay: 1000,
  });
});

mock.onPost("/customers").reply(config => {
  return respond({
    config,
    errorProbability: 0,
    timeoutProbability: 50,
    serverErrorProbability: 90,
    delay: 1000,
    onSuccess: value => {
      const length = customers.push(value);
      value.id = length - 1;
      return {
        id: value.id,
      };
    }
  });
});

const deleteUrl = /\/customers\/(\d+)/;
mock.onDelete(deleteUrl).reply(config => {
  return respond({
    config,
    errorProbability: 0,
    timeoutProbability: 50,
    serverErrorProbability: 90,
    delay: 1000,
    onSuccess: () => {
      const id = parseInt(config.url.match(deleteUrl)[1], 10);
      const customerIndex = customers.map(row => row.id).indexOf(id);
      customers.splice(customerIndex, 1);
      return {
        id
      };
    }
  });
});

mock.onGet("/contacts").reply(config => {
  const startIndex = hasPagination(config.params) ? config.params.size * config.params.page : 0;
  const endIndex = hasPagination(config.params) ? startIndex + config.params.size : contacts.length;
  return respond({
    config,
    result: { data: contacts.slice(startIndex, endIndex), count: contacts.length},
    errorProbability: 0,
    timeoutProbability: 50,
    serverErrorProbability: 90,
    delay: 1000,
  });
});