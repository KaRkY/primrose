import { normalize } from "normalizr";
import schema from "./schema";

export default {
  accounts: {
    query: `
    query testing($page: Int, $size: Int, $sort: String){
      count: accountsCount
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
    transform: (data) => {
      const normalized = normalize(data.data.accounts, schema.accounts);
      return {
        count: data.count,
        entities: normalized.entities,
        result: normalized.result,
      };
    },
    field: "accounts",
  },
};
