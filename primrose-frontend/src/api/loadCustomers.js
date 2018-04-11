const relationTypes = ["Customer", "Partner", "Investor", "Reseller"];
const types = ["Person", "Company"];

const count = 24;

const generate = count => {
  const customers = [];
  for (let i = 0; i < count; i++) {
    const customer = {
      id: i,
      relationType: relationTypes[i % 4],
      type: types[i % 2],
      fullName: `Customer ${i}`,
      displayName: `Customer ${i}`,
    };

    customers.push(customer);
  }
  return customers;
};

const generatedCustomers = generate(count);

export default (query = { page: 0, size: 5 }) => {
  return new Promise(resolve => setTimeout(() => {
      const startindex = query.size * query.page;
      const endindex = startindex + query.size;

      resolve({
        customers: generatedCustomers.slice(startindex, endindex),
        count,
      });
    },
    Math.floor((Math.random() * 4000) + 1)
  ));
};