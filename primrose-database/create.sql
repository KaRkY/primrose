/*
 * 
 * TABLES
 * 
 */
create table t_account_types(
  account_type_id   bigint  constraint nn_account_types_account_type_id not null,
  account_type      text    constraint nn_account_types_account_type    not null,
  
  constraint pk_account_types primary key (account_type_id)
);
comment on table t_account_types is 'Enumeration of diferent account types.';

create table t_accounts(
  account_code        text                      constraint nn_accounts_account_code not null,
  account_url         text                      constraint nn_accounts_account_url not null default '',
  account_type_id     bigint                    constraint nn_accounts_account_type not null,
  parent_account_code text,
  display_name        text,
  full_name           text,
  email               text,
  phone               text,
  website             text,
  description         text,
  
  valid_from          timestamp with time zone  constraint nn_accounts_valid_from   not null,
  valid_to            timestamp with time zone,
  
  constraint pk_accounts                primary key (account_code),
  constraint ck_accounts_positive_range check (valid_to > valid_from),
  constraint fk_account_parent_account  foreign key (parent_account_code) references t_accounts(account_code),
  constraint fk_account_type            foreign key (account_type_id)     references t_account_types(account_type_id)
);
comment on table t_accounts is 'Actual account data.';

create table t_account_search(
  account_code      text      constraint nn_accounts_account_code not null,
  full_text_search  tsvector,
  
  constraint pk_account_search          primary key (account_code),
  constraint fk_account_search_account  foreign key (account_code)     references t_accounts(account_code)
);
comment on table t_account_search is 'Search index for Accounts with types.';

create table t_addresses(
  address_id        bigint  constraint nn_addresses_address_id  not null,
  address_url       text    constraint nn_accounts_account_url not null default '',
  street            text,
  city              text,
  postal_code       text,
  state             text,
  country           text,
  
  constraint pk_addresses primary key (address_id)
);
comment on table t_addresses is 'Simple address data.';

create table t_address_search(
  address_id        bigint  constraint nn_addresses_address_id  not null,
  full_text_search  tsvector,
  
  constraint pk_address_search          primary key (address_id),
  constraint fk_address_search_address  foreign key (address_id)     references t_addresses(address_id)
);
comment on table t_account_search is 'Search index for Addresses with types.';

create table t_account_address_types(
  account_address_type_id bigint  constraint nn_account_address_types not null,
  account_address_type    text,
  
  constraint pk_account_address_types primary key (account_address_type_id)
);
comment on table t_account_address_types is 'Enumeration of address types.';

create table t_account_addresses(
  account_code            text    constraint nn_account_addresses_account_code          not null,
  address_id              bigint  constraint nn_account_addresses_address_is            not null,
  account_address_type_id bigint  constraint nn_account_addresses_account_address_type  not null,
  
  constraint pk_account_addresses       primary key (account_code, address_id),
  constraint fk_account_address_account foreign key (account_code)            references t_accounts(account_code),
  constraint fk_account_address_address foreign key (address_id)              references t_addresses(address_id),
  constraint fk_account_address_type    foreign key (account_address_type_id) references t_account_address_types(account_address_type_id)
);
comment on table t_account_addresses is 'Account address by type.';

create table t_contacts(
  contact_id        bigint  constraint nn_contacts_contact_id not null,
  contact_url       text    constraint nn_accounts_account_url not null default '',
  person_name       text,
  email             text,
  phone             text,
  address_id        bigint,
  
  constraint pk_contacts  primary key (contact_id)
);
comment on table t_contacts is 'Contact data.';

create table t_contact_search(
  contact_id        bigint  constraint nn_contacts_contact_id not null,
  full_text_search  tsvector,
  
  constraint pk_contact_search          primary key (contact_id),
  constraint fk_contact_search_contact  foreign key (contact_id)     references t_contacts(contact_id)
);
comment on table t_account_search is 'Search index for Addresses with types.';

create table t_account_contact_titles(
  account_contact_title_id  bigint  constraint nn_account_contact_titles_account_contact_title_id not null,
  account_contact_title     text,
  
  constraint pk_account_contact_titles  primary key (account_contact_title_id)
);
comment on table t_account_contact_titles is 'Enumeration of contact titles.';

create table t_account_contacts(
  account_code              text    constraint nn_account_contacts_account_code not null,
  contact_id                bigint  constraint nn_account_contacts_contact_id   not null,
  account_contact_title_id  bigint,
  
  constraint pk_account_contacts              primary key (account_code, contact_id),
  constraint fk_account_contact_account       foreign key (account_code)              references t_accounts(account_code),
  constraint fk_account_contact_contact       foreign key (contact_id)                references t_contacts(contact_id),
  constraint fk_account_contact_contact_title foreign key (account_contact_title_id)  references t_account_contact_titles(account_contact_title_id)
);
comment on table t_account_contacts is 'Account contacts by title.';

/*
 * 
 * INDEXES
 * 
 */
create unique index idx_account_contact_title on t_account_contact_titles(account_contact_title);
create unique index idx_account_url           on t_accounts(account_url);
create unique index idx_address_url           on t_addresses(address_url);
create unique index idx_contact_url           on t_contacts(contact_url);
create index idx_accounts_full_text_search    on t_account_search using GIN (full_text_search);
create index idx_addresses_full_text_search   on t_address_search using GIN (full_text_search);
create index idx_contacts_full_text_search    on t_address_search using GIN (full_text_search);

/*
 * 
 * SEQUENCES
 * 
 */
create sequence s_account_code    start with 100  increment by 1;
create sequence s_address         start with 1    increment by 1;
create sequence s_contact         start with 1    increment by 1;
create sequence s_contact_titles  start with 1    increment by 1;


/*
 * 
 * DATA
 * 
 */
insert into t_account_types(account_type_id, account_type) values 
(1, 'Customer'),
(2, 'Partner'),
(3, 'Investor'),
(4, 'Reseller');

insert into t_account_address_types(account_address_type_id, account_address_type) values
(1, 'Billing'),
(2, 'Shipping');

/*
 *
 * FUNCTIONS 
 *
 */
create function account_search_vector(account t_accounts) returns tsvector as $$
declare
  account_type text;
  result tsvector;
begin
  select aty.account_type into account_type
  from t_account_types aty
  where account_type_id = account.account_type_id;
  
  result := 
    to_tsvector('pg_catalog.english', account.account_code) ||
    to_tsvector('pg_catalog.english', coalesce(account.parent_account_code, '')) ||
    to_tsvector('pg_catalog.english', coalesce(account.display_name, '')) ||
    to_tsvector('pg_catalog.english', coalesce(account.full_name, '')) ||
    to_tsvector('pg_catalog.english', coalesce(account.email, '')) ||
    to_tsvector('pg_catalog.english', coalesce(account.phone, '')) ||
    to_tsvector('pg_catalog.english', coalesce(account.website, '')) ||
    to_tsvector('pg_catalog.english', coalesce(account.description, '')) ||
    to_tsvector('pg_catalog.english', coalesce(account_type, ''));
    
    return result;
end
$$ LANGUAGE plpgsql;

create function address_search_vector(address t_addresses) returns tsvector as $$
declare
  result tsvector;
begin
  result := 
    to_tsvector('pg_catalog.english', address.address_id::text) ||
    to_tsvector('pg_catalog.english', coalesce(address.street, '')) ||
    to_tsvector('pg_catalog.english', coalesce(address.city, '')) ||
    to_tsvector('pg_catalog.english', coalesce(address.postal_code, '')) ||
    to_tsvector('pg_catalog.english', coalesce(address.state, '')) ||
    to_tsvector('pg_catalog.english', coalesce(address.country, ''));
    
    return result;
end
$$ LANGUAGE plpgsql;

create function contact_search_vector(contact t_contacts) returns tsvector as $$
declare
  address t_addresses;
  result tsvector;
begin
  
  select * into address from t_addresses a where a.address_id = contact.address_id;
  result := 
    to_tsvector('pg_catalog.english', contact.contact_id::text) ||
    to_tsvector('pg_catalog.english', coalesce(contact.person_name, '')) ||
    to_tsvector('pg_catalog.english', coalesce(contact.email, '')) ||
    to_tsvector('pg_catalog.english', coalesce(contact.phone, '')) ||
    address_search_vector(address);
    
    return result;
end
$$ LANGUAGE plpgsql;

create function account_full_text_search_insert() returns trigger as $$
begin
  insert into t_account_search(account_code, full_text_search) values (new.account_code, account_search_vector(new));
  return new;
end
$$ LANGUAGE plpgsql;

create function account_full_text_search_update() returns trigger as $$
begin
	update t_account_search set full_text_search = account_search_vector(new) where account_code = new.account_code;
  return new;
end
$$ LANGUAGE plpgsql;

create function address_full_text_search_insert() returns trigger as $$
begin
  insert into t_address_search(address_id, full_text_search) values (new.address_id, address_search_vector(new));
  return new;
end
$$ LANGUAGE plpgsql;

create function address_full_text_search_update() returns trigger as $$
begin
  update t_address_search set full_text_search = address_search_vector(new) where address_id = new.address_id;
  update t_contact_search c set full_text_search = contact_search_vector(c) where c.address_id = new.address_id;
  return new;
end
$$ LANGUAGE plpgsql;

create function contact_full_text_search_insert() returns trigger as $$
begin
  insert into t_contact_search(contact_id, full_text_search) values (new.contact_id, contact_search_vector(new));
  return new;
end
$$ LANGUAGE plpgsql;

create function contact_full_text_search_update() returns trigger as $$
begin
  update t_contact_search set full_text_search = contact_search_vector(new) where contact_id = new.contact_id;
  return new;
end
$$ LANGUAGE plpgsql;

create function account_unique_url() returns trigger as $$
declare
  key TEXT;
  qry TEXT;
  found TEXT;
begin

  loop

    -- Generate our string bytes and re-encode as a base64 string.
    key := encode(gen_random_bytes(6), 'base64');

    -- Base64 encoding contains 2 URL unsafe characters by default.
    -- The URL-safe version has these replacements.
    key := replace(key, '/', '_'); -- url safe replacement
    key := replace(key, '+', '-'); -- url safe replacement

    

    if not exists( select 1 from t_accounts where account_url = key) then
      exit;
    end if;
  end loop;

  new.account_url = key;

  return new;
end;
$$ LANGUAGE plpgsql;

create function address_unique_url() returns trigger as $$
declare
  key TEXT;
  qry TEXT;
  found TEXT;
begin

  loop

    -- Generate our string bytes and re-encode as a base64 string.
    key := encode(gen_random_bytes(6), 'base64');

    -- Base64 encoding contains 2 URL unsafe characters by default.
    -- The URL-safe version has these replacements.
    key := replace(key, '/', '_'); -- url safe replacement
    key := replace(key, '+', '-'); -- url safe replacement

    

    if not exists( select 1 from t_addresses where address_url = key) then
      exit;
    end if;
  end loop;

  new.address_url = key;

  return new;
end;
$$ LANGUAGE plpgsql;

create function contact_unique_url() returns trigger as $$
declare
  key TEXT;
  qry TEXT;
  found TEXT;
begin

  loop

    -- Generate our string bytes and re-encode as a base64 string.
    key := encode(gen_random_bytes(6), 'base64');

    -- Base64 encoding contains 2 URL unsafe characters by default.
    -- The URL-safe version has these replacements.
    key := replace(key, '/', '_'); -- url safe replacement
    key := replace(key, '+', '-'); -- url safe replacement

    

    if not exists( select 1 from t_contacts where contact_url = key) then
      exit;
    end if;
  end loop;

  new.contact_url = key;

  return new;
end;
$$ LANGUAGE plpgsql;


create trigger search_insert  after   insert on t_accounts  for each row execute procedure account_full_text_search_insert();
create trigger search_update  after   update on t_accounts  for each row execute procedure account_full_text_search_update();
create trigger search_insert  after   insert on t_addresses for each row execute procedure address_full_text_search_insert();
create trigger search_update  after   update on t_addresses for each row execute procedure address_full_text_search_update();
create trigger search_insert  after   insert on t_contacts  for each row execute procedure contact_full_text_search_insert();
create trigger search_update  after   update on t_contacts  for each row execute procedure contact_full_text_search_update();
create trigger account_url    before  insert on t_accounts  for each row execute procedure account_unique_url();
create trigger address_url    before  insert on t_addresses for each row execute procedure address_unique_url();
create trigger contact_url    before  insert on t_contacts  for each row execute procedure contact_unique_url();