drop schema if exists primrose cascade;
create schema primrose;
/*
 * 
 * TABLES
 * 
 */

create table account_types(
  id    bigint                          constraint  nn_account_types_id         not null,
  name  text                            constraint  nn_account_types_name       not null,
  
  constraint pk_account_types             primary key (id)
);
comment on table account_types is 'Enumeration of diferent account types.';

create table accounts(
  id              bigint                    constraint nn_account_id            not null,
  account_type    bigint                    constraint nn_account_type          not null,
  parent_account  bigint,
  name            text                      constraint nn_account_name          not null,
  display_name    text,
  email           text,
  phone           text,
  website         text,
  description     text,
  valid_from      timestamp with time zone  constraint  nn_accounts_valid_from  not null  default current_timestamp,
  valid_to        timestamp with time zone,
  
  constraint pk_accounts                  primary key (id),
  constraint ck_accounts_positive_range   check (valid_to > valid_from),
  constraint fk_accounts_parent_account   foreign key (parent_account)  references accounts(id),
  constraint fk_accounts_type_type        foreign key (account_type)    references account_types(id)
);
comment on table accounts is 'Actual account data.';

create table addresses(
  id            bigint                    constraint  nn_addresses_id             not null,
  street        text                      constraint  nn_addresses_street         not null,
  street_number text                      constraint  nn_addresses_street_number  not null,
  city          text                      constraint  nn_addresses_city           not null,
  postal_code   text                      constraint  nn_addresses_postal_code    not null,
  state         text,
  country       text                      constraint  nn_addresses_country        not null,
  
  constraint pk_addresses             primary key (id),
  constraint uq_addresses             unique (street, street_number, city, postal_code, state, country)
);
comment on table addresses is 'Simple address data.';

create table account_address_types(
  id          bigint                    constraint  nn_account_address_types_id         not null,
  name        text                      constraint  nn_account_address_types_name       not null  constraint uq_account_address_types_name  unique,
  
  constraint pk_account_address_types primary key (id)
);
comment on table account_address_types is 'Enumeration of address types.';

create table account_addresses(
  account               bigint                    constraint nn_account_addresses_account               not null,
  address               bigint                    constraint nn_account_addresses_address               not null,
  account_address_type  bigint                    constraint nn_account_addresses_account_address_type  not null,
  
  constraint pk_account_addresses           primary key (account, address),
  constraint fk_account_address_account     foreign key (account)               references accounts(id),
  constraint fk_account_address_address     foreign key (address)               references addresses(id),
  constraint fk_account_address_type        foreign key (account_address_type)  references account_address_types(id)
);
comment on table account_addresses is 'Account address by type.';

create table contacts(
  id          bigint                    constraint  nn_contacts_id          not null,
  name        text                      constraint  nn_contacts_name        not null,
  email       text                      constraint  nn_contacts_email       not null,
  phone       text                      constraint  nn_contacts_phone       not null,
  
  constraint pk_contacts            primary key (id)
);
comment on table contacts is 'Contact data.';

create table account_contact_types(
  id          bigint                    constraint  nn_account_contact_types_id         not null,
  name        text                      constraint  nn_account_contact_types_name       not null  constraint uq_account_contact_types_name   unique,
  
  constraint pk_account_contact_types             primary key (id)
);
comment on table account_contact_types is 'Enumeration of contact types.';

create table account_contacts(
  account               bigint                    constraint  nn_account_contacts_account     not null,
  contact               bigint                    constraint  nn_account_contacts_contact     not null,
  account_contact_type  bigint,
  
  constraint pk_account_contacts              primary key (account, contact),
  constraint fk_account_contacts_account      foreign key (account)               references accounts(id),
  constraint fk_account_contacts_contact      foreign key (contact)               references contacts(id),
  constraint fk_account_contacts_contact_type foreign key (account_contact_type)  references account_contact_types(id)
);
comment on table account_contacts is 'Account contacts by type.';

/*
 * 
 * INDEXES
 * 
 */

/*
 * 
 * SEQUENCES
 * 
 */
create sequence account_types_seq         start with 1  increment by 1;
create sequence accounts_seq              start with 1  increment by 1;
create sequence addresses_seq             start with 1  increment by 1;
create sequence account_address_types_seq start with 1  increment by 1;
create sequence contacts_seq              start with 1  increment by 1;
create sequence account_contact_types_seq start with 1  increment by 1;

/*
 *
 * FUNCTIONS 
 *
 */
create function pseudo_encrypt(value int) returns int as $$
declare
l1 int;
l2 int;
r1 int;
r2 int;
i int:=0;
begin
 l1:= (value >> 16) & 65535;
 r1:= value & 65535;
 while i < 3 loop
   l2 := r1;
   r2 := l1 # ((((1366 * r1 + 150889) % 714025) / 714025.0) * 32767)::int;
   l1 := l2;
   r1 := r2;
   i := i + 1;
 end loop;
 return ((r1 << 16) + l1);
end;
$$ LANGUAGE plpgsql;

create function pseudo_encrypt(value bigint) returns bigint as $$
declare
l1 bigint;
l2 bigint;
r1 bigint;
r2 bigint;
i int:=0;
begin
    l1:= (value >> 32) & 4294967295::bigint;
    r1:= value & 4294967295;
    while i < 3 loop
        l2 := r1;
        r2 := l1 # ((((1366.0 * r1 + 150889) % 714025) / 714025.0) * 2147483647)::int;
        raise notice 'test %', r2;
        l1 := l2;
        r1 := r2;
        i := i + 1;
    end loop;
return ((r1::bigint << 32) + l1);
end;
$$ language plpgsql;

create function number_to_base(num bigint, base integer) returns text as $$
with recursive n(i, n, r) as (
    select -1, num, 0
  union all
    select i + 1, n / base, (n % base)::int
    from n
    where n > 0
)
select string_agg(ch, '')
from (
  select case
           when r between 0 and 9 then r::text
           when r between 10 and 35 then chr(ascii('a') + r - 10)
           else '%'
         end ch
  from n
  where i >= 0
  order by i desc
) ch
$$ language sql;

create function number_from_base(num text, base integer) returns numeric as $$
select sum(exp * cn)
from (
  select base::numeric ^ (row_number() over () - 1) exp,
         case
           when ch between '0' and '9' then ascii(ch) - ascii('0')
           when ch between 'a' and 'z' then 10 + ascii(ch) - ascii('a')
         end cn
  from regexp_split_to_table(reverse(lower(num)), '') ch(ch)
) sub
$$ language sql;

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

create function unique_code() returns trigger as $$
begin
  new.code = stringify_bigint(pseudo_encrypt(new.id));
  return new;
end;
$$ LANGUAGE plpgsql;

/*
 * 
 * DATA
 * 
 */
insert into account_types(id, name) values 
(1, 'Customer'),
(2, 'Partner'),
(3, 'Investor'),
(4, 'Reseller');

insert into account_address_types(id, name) values
(1, 'Billing'),
(2, 'Shipping');

insert into account_contact_types(id, name) values
(1, 'Seller'),
(2, 'Manager'),
(5, 'Developer'),
(6, 'Consultant');