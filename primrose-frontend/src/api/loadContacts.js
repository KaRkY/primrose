const count = 24;

const generate = count => {
  const contacts = [];
  for (let i = 0; i < count; i++) {
    const contact = {
      id: i,
      name: `Contact ${i}`,
      primaryEmail: `Contact.${i}@email.com`,
      primaryPhone: "040404040",
    };

    contacts.push(contact);
  }
  return contacts;
};

const generatedContacts = generate(count);

export default (query = { page: 0, size: 5 }) => {
  return new Promise(resolve => setTimeout(() => {
      const startindex = query.size * query.page;
      const endindex = startindex + query.size;

      resolve({
        contacts: generatedContacts.slice(startindex, endindex),
        count,
      });
    },
    Math.floor((Math.random() * 4000) + 1)
  ));
};