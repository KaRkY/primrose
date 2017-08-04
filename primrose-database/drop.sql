/*
 * 
 * FOREIGN KEYS
 * 
 */
alter table if exists t_accounts
drop constraint if exists fk_account_type;

alter table if exists t_account_search
drop constraint if exists fk_account_search_account;

alter table if exists t_address_search
drop constraint if exists fk_address_search_address;

alter table if exists t_account_addresses
drop constraint if exists fk_account_address_account;

alter table if exists t_account_addresses
drop constraint if exists fk_account_address_address;

alter table if exists t_account_addresses
drop constraint if exists fk_account_address_type;

alter table if exists t_contact_search
drop constraint if exists fk_contact_search_contact;

alter table if exists t_account_contacts
drop constraint if exists fk_account_contact_account;

alter table if exists t_account_contacts
drop constraint if exists fk_account_contact_contact;

alter table if exists t_account_contacts
drop constraint if exists fk_account_contact_contact_title;

/*
 * 
 * FUNCTIONS
 * 
 */
drop function if exists account_search_vector(account t_accounts);
drop function if exists address_search_vector(address t_addresses);
drop function if exists contact_search_vector(contact t_contacts);

/*
 * 
 * INDEXES
 * 
 */
drop index if exists idx_account_contact_title;
drop index if exists idx_account_url;
drop index if exists idx_address_url;
drop index if exists idx_contact_url;
drop index if exists idx_accounts_full_text_search;
drop index if exists idx_addresses_full_text_search;
drop index if exists idx_contacts_full_text_search;

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
drop table if exists t_account_contact_titles;

/*
 * 
 * SEQUENCES
 * 
 */
drop sequence if exists s_account_code;
drop sequence if exists s_address;
drop sequence if exists s_contact;
drop sequence if exists s_contact_titles;

/*
 * 
 * TRIGGER FUNCTIONS
 * 
 */
drop function if exists account_full_text_search_insert();
drop function if exists account_full_text_search_update();
drop function if exists address_full_text_search_insert();
drop function if exists address_full_text_search_update();
drop function if exists contact_full_text_search_insert();
drop function if exists contact_full_text_search_update();
drop function if exists account_unique_url();
drop function if exists address_unique_url();
drop function if exists contact_unique_url();