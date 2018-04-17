import range from "lodash/range";
import ax from "axios";
import queryString from "query-string";
import MockAdapter from "axios-mock-adapter";

const relationTypes = {
  customer: "Customer",
  partner: "Partner",
  investor: "Investor",
  reseller: "Reseller",
};;
const types = {
  person: "Person",
  company: "Company"
};

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
    relationType: relationTypes[Object.keys(relationTypes)[index % 4]],
    type: types[Object.keys(types)[index % 2]],
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


const axios = ax.create({
  paramsSerializer: queryString.stringify,
  timeout: 20000,
});

const axiosMock = ax.create({
  paramsSerializer: queryString.stringify,
  timeout: 2000,
});
export default axios;
const mock = new MockAdapter(axiosMock);

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
        if (isTimeout(timeoutProbability)) {
          const error = new Error("timeout of " + config.timeout + "ms exceeded");
          error.config = config;
          error.code = "ECONNABORTED";
          return reject(error);
        } else {
          if (isServerError(serverErrorProbability)) {
            resolve([500, {
              error: true
            }]);
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

const configureEntity = (mocker, entity, values) => {
  mocker.onGet(`/${entity}`).reply(config => {
    const startIndex = hasPagination(config.params) ? config.params.size * config.params.page : 0;
    const endIndex = hasPagination(config.params) ? startIndex + config.params.size : customers.length;
    return respond({
      config,
      result: {
        data: values.slice(startIndex, endIndex),
        count: values.length
      },
      errorProbability: 0,
      timeoutProbability: 50,
      serverErrorProbability: 90,
      delay: 1000,
    });
  });
  
  mocker.onPost(`/${entity}`).reply(config => {
    return respond({
      config,
      errorProbability: 0,
      timeoutProbability: 50,
      serverErrorProbability: 90,
      delay: 1000,
      onSuccess: value => {
        const length = values.push(value);
        value.id = length - 1;
        return {
          id: value.id,
        };
      }
    });
  });

  const putUrl = new RegExp(`/${entity}/(\\d+)`);
  mocker.onPut(putUrl).reply(config => {
    return respond({
      config,
      errorProbability: 0,
      timeoutProbability: 50,
      serverErrorProbability: 90,
      delay: 1000,
      onSuccess: value => {
        const id = parseInt(config.url.match(putUrl)[1], 10);
        const index = values.map(row => row.id).indexOf(id);
        values.splice(index, 1, value);
        return value;
      }
    });
  });
  
  mocker.onDelete(`/${entity}`).reply(config => {
    return respond({
      config,
      errorProbability: 0,
      timeoutProbability: 50,
      serverErrorProbability: 90,
      delay: 1000,
      onSuccess: value => {
        config.params.id.forEach(id => {
          const index = values.map(row => row.id).indexOf(id);
          values.splice(index, 1);
        });
        return {
          id: config.params.id,
        };
      }
    });
  });
  
  const deleteUrl = new RegExp(`/${entity}/(\\d+)`);
  mocker.onDelete(deleteUrl).reply(config => {
    return respond({
      config,
      errorProbability: 0,
      timeoutProbability: 50,
      serverErrorProbability: 90,
      delay: 1000,
      onSuccess: () => {
        const id = parseInt(config.url.match(deleteUrl)[1], 10);
        const index = values.map(row => row.id).indexOf(id);
        values.splice(index, 1);
        return {
          id: [id],
        };
      }
    });
  });
};

configureEntity(mock, "customers", customers);
configureEntity(mock, "contacts", contacts);