/*
 * 
 * FOREIGN KEYS
 * 
 */
alter table if exists t_accounts
drop constraint if exists fk_account_type;

alter table if exists t_account_addresses
drop constraint if exists fk_account_address_account;

alter table if exists t_account_addresses
drop constraint if exists fk_account_address_address;

alter table if exists t_account_addresses
drop constraint if exists fk_account_address_type;

alter table if exists t_account_contact_types
drop constraint if exists fk_account_contact_type_parent;

alter table if exists t_account_contacts
drop constraint if exists fk_account_contact_account;

alter table if exists t_account_contacts
drop constraint if exists fk_account_contact_contact;

alter table if exists t_account_contacts
drop constraint if exists fk_account_contact_contact_type;

/*
 * 
 * FUNCTIONS
 * 
 */
drop function if exists generate_random_string_base36(size int);
drop function if exists account_search_vector(account t_accounts);
drop function if exists address_search_vector(address t_addresses);
drop function if exists contact_search_vector(contact t_contacts);

/*
 * 
 * INDEXES
 * 
 */
drop index if exists idx_account_contact_type;
drop index if exists idx_account_code;
drop index if exists idx_address_code;
drop index if exists idx_contact_code;

/*
 * 
 * TABLES
 * 
 */
drop table if exists t_account_types;
drop table if exists t_accounts;
drop table if exists t_account_search;
drop table if exists t_addresses;
drop table if exists t_address_search;
drop table if exists t_account_address_types;
drop table if exists t_account_addresses;
drop table if exists t_contacts;
drop table if exists t_contact_search;
drop table if exists t_account_contacts;
drop table if exists t_account_contact_types;

/*
 * 
 * SEQUENCES
 * 
 */
drop sequence if exists s_account;
drop sequence if exists s_address;
drop sequence if exists s_contact;
drop sequence if exists s_contact_types;

/*
 * 
 * TRIGGER FUNCTIONS
 * 
 */
drop function if exists account_unique_code();
drop function if exists address_unique_code();
drop function if exists contact_unique_code();