drop schema if exists primrose cascade;
create schema primrose;
/*
 * 
 * TABLES
 * 
 */

create table customer_relation_types(
  id    bigint  not null,
  name  text    not null  unique,
  
  primary key (id)
);

create table customer_types(
  id    bigint  not null,
  name  text    not null  unique,
  
  primary key (id)
);

create table customers(
  id                      bigint                    not null,
  customer_type           bigint                    not null,
  customer_relation_type  bigint                    not null,
  full_name               text                      not null,
  display_name            text,
  email                   text,
  phone                   text,
  description             text,
  
  primary key (id),
  foreign key (customer_type)             references customer_types(id),
  foreign key (customer_relation_type)    references customer_relation_types(id)
);

create table accounts(
  id        bigint  not null,
  name      text    not null,
  customer  bigint  not null,
  
  primary key (id),
  foreign key (customer)  references customers(id)
);

create table addresses(
  id            bigint  not null,
  street        text    not null,
  street_number text    not null,
  city          text    not null,
  postal_code   text    not null,
  state         text,
  country       text    not null,
  
  primary key (id)
);

create table address_types(
  id          bigint  not null,
  name        text    not null  unique,
  
  primary key (id)
);

create table customer_addresses(
  customer      bigint  not null,
  address       bigint  not null,
  address_type  bigint  not null,
  
  primary key (customer, address),
  foreign key (customer)      references customers(id),
  foreign key (address)       references addresses(id),
  foreign key (address_type)  references address_types(id)
);

create table account_addresses(
  account       bigint  not null,
  address       bigint  not null,
  address_type  bigint  not null,
  
  primary key (account, address),
  foreign key (account)       references accounts(id),
  foreign key (address)       references addresses(id),
  foreign key (address_type)  references address_types(id)
);

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
create sequence customer_types_seq          start with 1  increment by 1;
create sequence customer_relation_types_seq start with 1  increment by 1;
create sequence customer_seq                start with 1  increment by 1;
create sequence accounts_seq                start with 1  increment by 1;
create sequence addresses_seq               start with 1  increment by 1;
create sequence address_types_seq           start with 1  increment by 1;

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
insert into customer_types(id, name) values 
(1, 'Person'),
(2, 'Company');

insert into customer_relation_types(id, name) values 
(1, 'Customer'),
(2, 'Partner'),
(3, 'Investor'),
(4, 'Reseller');

insert into address_types(id, name) values
(1, 'Billing'),
(2, 'Shipping');