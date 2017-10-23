import schema from "./schema";

export default {
  accounts: {
    query: `
    query testing($page: Int, $size: Int, $sort: String){
      accounts(page: $page, size: $size, sort: $sort){
        id
        type
        displayName
        name
        email
        phone
        website
        description
        validFrom
        validTo
        addresses {
          id
          street
          streetNumber
          city
          postalCode
          state
          country
        }
        contacts {
          id
          name
          email
          phone
        }
      }
    }
    `,
    schema: schema.accounts,
    field: "accounts",
  },
};
