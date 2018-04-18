import contactsEntity from "./contacts";
import customersEntity from "./customers";
import accountsEntity from "./accounts";
import metaEntity from "./meta";

export const contacts = contactsEntity.reducer;
export const customers = customersEntity.reducer;
export const accounts = accountsEntity.reducer;
export const meta = metaEntity.reducer;

export { default as page } from "./page";
export { default as title } from "./title";