drop schema if exists primrose cascade;
create schema primrose;

/*
 * 
 * ENUM TABLES
 * 
 */

create table customer_types(
  id    bigserial not null,
  slug  text      not null  unique,
  name  text      not null  unique,
  
  primary key (id)
);

insert into customer_types(slug, name) values 
('person', 'Person'),
('company', 'Company');


create table address_types(
  id    bigserial  not null,
  slug  text    not null  unique,
  name  text    not null  unique,
  
  primary key (id)
);

insert into address_types(slug, name) values
('billing', 'Billing'),
('shipping', 'Shipping');

create table customer_relation_types(
  id    bigserial not null,
  slug  text      not null  unique,
  name  text      not null  unique,
  
  primary key (id)
);

insert into customer_relation_types(slug, name) values 
('customer', 'Customer'),
('partner', 'Partner'),
('ivestor', 'Investor'),
('reseller', 'Reseller');

create table phone_number_types(
  id    bigserial not null,
  slug  text      not null  unique,
  name  text      not null  unique,
  
  primary key (id)
);

insert into phone_number_types(slug, name) values 
('work', 'Work'),
('home', 'Home'),
('other', 'Other');

create table email_types(
  id    bigserial not null,
  slug  text      not null  unique,
  name  text      not null  unique,
  
  primary key (id)
);

insert into email_types(slug, name) values 
('work', 'Work'),
('home', 'Home'),
('other', 'Other');

create table contact_types(
  id    bigserial not null,
  slug  text      not null  unique,
  name  text      not null  unique,
  
  primary key (id)
);

insert into contact_types(slug, name) values 
('sales', 'Sales'),
('manager', 'Manager');
/*
 * 
 * TABLES
 * 
 */

create table addresses(
  id            bigserial not null,
  street        text      not null,
  street_number text      not null,
  city          text      not null,
  postal_code   text      not null,
  state         text,
  country       text      not null,
  
  primary key (id)
);

create table phone_numbers(
  id    bigserial not null,
  phone text,
  
  primary key (id)
);

create table emails(
  id    bigserial not null,
  email text,
  
  primary key (id)
);

create table customers(
  id                      bigserial not null,
  slug                    text      not null  unique,
  customer_type           bigint    not null,
  customer_relation_type  bigint    not null,
  full_name               text      not null,
  display_name            text,
  description             text,
  
  primary key (id),
  foreign key (customer_type)           references customer_types(id),
  foreign key (customer_relation_type)  references customer_relation_types(id)
);

create table customer_addresses(
  address       bigint  not null,
  customer      bigint  not null,
  address_type  bigint  not null,
  
  primary key (address, customer),
  foreign key (address)       references addresses(id),
  foreign key (customer)      references customers(id),
  foreign key (address_type)  references address_types(id)
);

create table customer_phone_numbers(
  phone             bigint  not null,
  customer          bigint  not null,
  phone_number_type bigint,
  
  primary key (phone, customer),
  foreign key (phone)             references phone_numbers(id),
  foreign key (customer)          references customers(id),
  foreign key (phone_number_type) references phone_number_types(id)
);

create table customer_emails(
  email       bigint  not null,
  customer    bigint  not null,
  email_type  bigint,
  
  primary key (email, customer),
  foreign key (email)       references emails(id),
  foreign key (customer)    references customers(id),
  foreign key (email_type)  references email_types(id)
);

create table accounts(
  id          bigserial not null,
  customer    bigint    not null,
  slug        text      not null  unique,
  name        text      not null,
  description text,
  
  primary key (id),
  foreign key (customer)  references customers(id)
);

create table account_addresses(
  address       bigint  not null,
  account       bigint  not null,
  address_type  bigint  not null,
  
  primary key (address, account),
  foreign key (address)       references addresses(id),
  foreign key (account)       references accounts(id),
  foreign key (address_type)  references address_types(id)
);

create table account_phone_numbers(
  phone             bigint  not null,
  account           bigint  not null,
  phone_number_type bigint,
  
  primary key (phone, account),
  foreign key (phone)             references phone_numbers(id),
  foreign key (account)           references accounts(id),
  foreign key (phone_number_type) references phone_number_types(id)
);

create table account_emails(
  email       bigint  not null,
  account     bigint  not null,
  email_type  bigint,
  
  primary key (email, account),
  foreign key (email)       references emails(id),
  foreign key (account)     references accounts(id),
  foreign key (email_type)  references email_types(id)
);

create table contacts(
  id          bigserial not null,
  slug        text      not null unique,
  full_name   text      not null,
  description text,
  
  primary key (id)
);

create table contact_addresses(
  address       bigint  not null,
  contact       bigint  not null,
  address_type  bigint  not null,
  
  primary key (address, contact),
  foreign key (address)       references addresses(id),
  foreign key (contact)       references contacts(id),
  foreign key (address_type)  references address_types(id)
);

create table contact_phone_numbers(
  phone             bigint  not null,
  contact           bigint  not null,
  phone_number_type bigint,
  
  primary key (phone, contact),
  foreign key (phone)             references phone_numbers(id),
  foreign key (contact)           references contacts(id),
  foreign key (phone_number_type) references phone_number_types(id)
);

create table contact_emails(
  email       bigint  not null,
  contact     bigint  not null,
  email_type  bigint,
  
  primary key (email, contact),
  foreign key (email)       references emails(id),
  foreign key (contact)     references contacts(id),
  foreign key (email_type)  references email_types(id)
);

create table customer_contacts(
  contact       bigint  not null,
  customer      bigint  not null,
  contact_type  bigint not null,
  
  primary key (contact, customer),
  foreign key (contact)       references contacts(id),
  foreign key (customer)      references customers(id),
  foreign key (contact_type)  references contact_types(id)
);

create table account_contacts(
  contact       bigint  not null,
  account       bigint  not null,
  contact_type  bigint not null,
  
  primary key (contact, account),
  foreign key (contact)       references contacts(id),
  foreign key (account)       references accounts(id),
  foreign key (contact_type)  references contact_types(id)
);

/*
 * 
 * INDEXES
 * 
 */

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