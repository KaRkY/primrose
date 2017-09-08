/*
 * 
 * TABLES
 * 
 */
create table t_account_types(
  account_type_id   bigint  constraint nn_account_types_account_type_id   not null,
  account_type_code text    constraint nn_account_types_account_type_code not null,
  
  constraint pk_account_types primary key (account_type_id)
);
comment on table t_account_types is 'Enumeration of diferent account types.';

create table t_accounts(
  account_id          bigint                    constraint nn_account_types_account_type_id not null,
  account_code        text                      constraint nn_accounts_account_code not null default '',
  account_type_id     bigint                    constraint nn_accounts_account_type not null,
  parent_account_id   bigint,
  display_name        text,
  full_name           text,
  email               text,
  phone               text,
  website             text,
  description         text,
  
  valid_from          timestamp with time zone  constraint nn_accounts_valid_from   not null,
  valid_to            timestamp with time zone,
  
  constraint pk_accounts                primary key (account_id),
  constraint ck_accounts_positive_range check (valid_to > valid_from),
  constraint fk_account_parent_account  foreign key (parent_account_id)   references t_accounts(account_id),
  constraint fk_account_type            foreign key (account_type_id)     references t_account_types(account_type_id)
);
comment on table t_accounts is 'Actual account data.';

create table t_account_search(
  account_id        bigint      constraint nn_accounts_account_id not null,
  full_text_search  tsvector,
  
  constraint pk_account_search          primary key (account_id),
  constraint fk_account_search_account  foreign key (account_id)     references t_accounts(account_id)
);
comment on table t_account_search is 'Search index for Accounts with types.';

create table t_addresses(
  address_id        bigint  constraint nn_addresses_address_id    not null,
  address_code      text    constraint nn_addresses_address_code  not null default '',
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
  account_address_type_id   bigint  constraint nn_account_address_type_id   not null,
  account_address_type_code text    constraint nn_account_address_type_code not null,
  
  constraint pk_account_address_types primary key (account_address_type_id)
);
comment on table t_account_address_types is 'Enumeration of address types.';

create table t_account_addresses(
  account_id              bigint  constraint nn_account_addresses_account_id            not null,
  address_id              bigint  constraint nn_account_addresses_address_is            not null,
  account_address_type_id bigint  constraint nn_account_addresses_account_address_type  not null,
  
  constraint pk_account_addresses       primary key (account_id, address_id),
  constraint fk_account_address_account foreign key (account_id)              references t_accounts(account_id),
  constraint fk_account_address_address foreign key (address_id)              references t_addresses(address_id),
  constraint fk_account_address_type    foreign key (account_address_type_id) references t_account_address_types(account_address_type_id)
);
comment on table t_account_addresses is 'Account address by type.';

create table t_contacts(
  contact_id        bigint  constraint nn_contacts_contact_id   not null,
  contact_code      text    constraint nn_contacts_contact_code not null default '',
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

create table t_account_contact_types(
  account_contact_type_id     bigint  constraint nn_account_contact_types_account_contact_type_id   not null,
  account_contact_type_code   text    constraint nn_account_contact_types_account_contact_type_code not null,
  account_contact_type_parent bigint,
  
  constraint pk_account_contact_types       primary key (account_contact_type_id),
  constraint fk_account_contact_type_parent foreign key (account_contact_type_parent) references t_account_contact_types(account_contact_type_id)
);
comment on table t_account_contact_types is 'Enumeration of contact types.';

create table t_account_contacts(
  account_id                bigint  constraint nn_account_contacts_account_id   not null,
  contact_id                bigint  constraint nn_account_contacts_contact_id   not null,
  account_contact_type_id  bigint,
  
  constraint pk_account_contacts              primary key (account_id, contact_id),
  constraint fk_account_contact_account       foreign key (account_id)              references t_accounts(account_id),
  constraint fk_account_contact_contact       foreign key (contact_id)              references t_contacts(contact_id),
  constraint fk_account_contact_contact_type  foreign key (account_contact_type_id) references t_account_contact_types(account_contact_type_id)
);
comment on table t_account_contacts is 'Account contacts by type.';

/*
 * 
 * INDEXES
 * 
 */
create unique index idx_account_contact_type  on t_account_contact_types(account_contact_type_code);
create unique index idx_account_code          on t_accounts(account_code);
create unique index idx_address_code          on t_addresses(address_code);
create unique index idx_contact_code          on t_contacts(contact_code);
create index idx_accounts_full_text_search    on t_account_search using GIN (full_text_search);
create index idx_addresses_full_text_search   on t_address_search using GIN (full_text_search);
create index idx_contacts_full_text_search    on t_address_search using GIN (full_text_search);

/*
 * 
 * SEQUENCES
 * 
 */
create sequence s_account       start with 1  increment by 1;
create sequence s_address       start with 1  increment by 1;
create sequence s_contact       start with 1  increment by 1;
create sequence s_contact_types start with 1  increment by 1;


/*
 * 
 * DATA
 * 
 */
insert into t_account_types(account_type_id, account_type_code) values 
(1, 'customer'),
(2, 'partner'),
(3, 'investor'),
(4, 'reseller');

insert into t_account_address_types(account_address_type_id, account_address_type_code) values
(1, 'billing'),
(2, 'shipping');

insert into t_account_contact_types(account_contact_type_id, account_contact_type_parent, account_contact_type_code) values
(1, null, 'seller'),
(2, null, 'manager'),
(5, null, 'developer'),
(6, null, 'consultant');

/*
 *
 * FUNCTIONS 
 *
 */
create function generate_random_string_base36(size int) returns text as $$
declare
  chars char[];
  counter int;
  result text;
begin
  chars := array['0','1','2','3','4','5','6','7','8','9'
      ,'a','b','c','d','e','f','g','h','i','j','k','l','m'
      ,'n','o','p','q','r','s','t','u','v','w','x','y','z'];
  
  for counter in 1..size
  loop
    result = concat(result, chars[floor(random() * 36 + 1)]);
  end loop;

  return result;
end;
$$ LANGUAGE plpgsql;

create function account_search_vector(account t_accounts) returns tsvector as $$
declare
  account_type text;
  result tsvector;
begin
  select aty.account_type_code into account_type
  from t_account_types aty
  where account_type_id = account.account_type_id;

  result := 
    to_tsvector('pg_catalog.english', account.account_id::text) ||
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
  insert into t_account_search(account_id, full_text_search) values (new.account_id, account_search_vector(new));
  return new;
end
$$ LANGUAGE plpgsql;

create function account_full_text_search_update() returns trigger as $$
begin
	update t_account_search set full_text_search = account_search_vector(new) where account_id = new.account_id;
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

create function account_unique_code() returns trigger as $$
declare
  key text;
  qry text;
  found text;
  min_size int;
begin
  min_size = 6;
  
  loop
    key := generate_random_string_base36(min_size);
    
    if not exists( select 1 from t_accounts where account_code = key) then
      exit;
    end if;
    
    min_size = min_size + 1;
  end loop;

  new.account_code = key;

  return new;
end;
$$ LANGUAGE plpgsql;

create function address_unique_code() returns trigger as $$
declare
  key text;
  qry text;
  found text;
  min_size int;
begin
  min_size = 6;
  
  loop
    key := generate_random_string_base36(min_size);
    
    if not exists( select 1 from t_addresses where address_code = key) then
      exit;
    end if;
    
    min_size = min_size + 1;
  end loop;

  new.address_code = key;

  return new;
end;
$$ LANGUAGE plpgsql;

create function contact_unique_code() returns trigger as $$
declare
  key text;
  qry text;
  found text;
  min_size int;
begin
  min_size = 6;
  
  loop
    key := generate_random_string_base36(min_size);
    
    if not exists( select 1 from t_contacts where contact_code = key) then
      exit;
    end if;
    
    min_size = min_size + 1;
  end loop;

  new.contact_code = key;

  return new;
end;
$$ LANGUAGE plpgsql;

create trigger search_insert  after   insert on t_accounts  for each row execute procedure account_full_text_search_insert();
create trigger search_update  after   update on t_accounts  for each row execute procedure account_full_text_search_update();
create trigger search_insert  after   insert on t_addresses for each row execute procedure address_full_text_search_insert();
create trigger search_update  after   update on t_addresses for each row execute procedure address_full_text_search_update();
create trigger search_insert  after   insert on t_contacts  for each row execute procedure contact_full_text_search_insert();
create trigger search_update  after   update on t_contacts  for each row execute procedure contact_full_text_search_update();
create trigger account_code   before  insert on t_accounts  for each row execute procedure account_unique_code();
create trigger address_code   before  insert on t_addresses for each row execute procedure address_unique_code();
create trigger contact_code   before  insert on t_contacts  for each row execute procedure contact_unique_code();