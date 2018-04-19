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
  sort  int       not null  unique,
  
  primary key (id)
);

insert into customer_types(slug, name, sort) values 
('person', 'Person', 1),
('company', 'Company', 2);

create table address_types(
  id    bigserial not null,
  slug  text      not null  unique,
  name  text      not null  unique,
  sort  int       not null  unique,
  
  primary key (id)
);

insert into address_types(slug, name, sort) values
('billing', 'Billing', 1),
('shipping', 'Shipping', 2);

create table customer_relation_types(
  id    bigserial not null,
  slug  text      not null  unique,
  name  text      not null  unique,
  sort  int       not null  unique,
  
  primary key (id)
);

insert into customer_relation_types(slug, name, sort) values 
('customer', 'Customer', 1),
('partner', 'Partner', 2),
('investor', 'Investor', 3),
('reseller', 'Reseller', 4);

create table phone_number_types(
  id    bigserial not null,
  slug  text      not null  unique,
  name  text      not null  unique,
  sort  int       not null  unique,
  
  primary key (id)
);

insert into phone_number_types(slug, name, sort) values 
('work', 'Work', 1),
('home', 'Home', 2),
('other', 'Other', 3);

create table email_types(
  id    bigserial not null,
  slug  text      not null  unique,
  name  text      not null  unique,
  sort  int       not null  unique,
  
  primary key (id)
);

insert into email_types(slug, name, sort) values 
('work', 'Work', 1),
('home', 'Home', 2),
('other', 'Other', 3);

create table contact_types(
  id    bigserial not null,
  slug  text      not null  unique,
  name  text      not null  unique,
  sort  int       not null  unique,
  
  primary key (id)
);

insert into contact_types(slug, name, sort) values 
('sales', 'Sales', 1),
('manager', 'Manager', 2);

create table meeting_types(
  id    bigserial not null,
  slug  text      not null  unique,
  name  text      not null  unique,
  sort  int       not null  unique,
  
  primary key (id)
);

insert into meeting_types(slug, name, sort) values 
('call', 'Call', 1),
('on-site', 'On Site', 2),
('email', 'Email', 3),
('mail', 'mail', 4);

/*
 * 
 * CODES
 * 
 */

create table customer_codes(
  id    bigserial not null,
  code  text      unique,
  
  primary key (id)
);

create table account_codes(
  id        bigserial not null,
  customer  bigint    not null,
  code      text      unique,
  
  primary key (id),
  foreign key (customer)  references customer_codes(id)
);

create table contact_codes(
  id    bigserial not null,
  code  text      unique,
  
  primary key (id)
);

create table meeting_codes(
  id    bigserial not null,
  code  text      unique,
  
  primary key (id)
);

/*
 * 
 * TABLES
 * 
 */

create table addresses(
  id            bigserial,
  street        text      not null,
  street_number text      not null,
  city          text      not null,
  postal_code   text      not null,
  state         text,
  country       text      not null,
  deleted       boolean   not null default false,
  
  primary key (id)
);

create table phone_numbers(
  id      bigserial,
  phone   text      not null unique,
  deleted boolean   not null default false,
  
  primary key (id)
);

create table emails(
  id      bigserial,
  email   text      not null unique,
  deleted boolean   not null default false,
  
  primary key (id)
);

create table customers(
  id                      bigserial not null,
  code                    bigint    not null,
  customer_type           bigint    not null,
  customer_relation_type  bigint    not null,
  full_name               text      not null,
  display_name            text,
  description             text,
  
  valid_from              timestamp with time zone not null default now(),
  valid_to                timestamp with time zone,
  
  primary key (id),
  foreign key (code)                    references customer_codes(id),
  foreign key (customer_type)           references customer_types(id),
  foreign key (customer_relation_type)  references customer_relation_types(id)
);

create table customer_addresses(
  address       bigint  not null,
  customer      bigint  not null,
  address_type  bigint  not null,

  valid_from    timestamp with time zone not null default now(),
  valid_to      timestamp with time zone,
  
  primary key (address, customer),
  foreign key (address)       references addresses(id),
  foreign key (customer)      references customer_codes(id),
  foreign key (address_type)  references address_types(id)
);

create table customer_phone_numbers(
  phone             bigint  not null,
  customer          bigint  not null,
  prim              bool    not null default false,
  phone_number_type bigint  not null,
  
  valid_from        timestamp with time zone not null default now(),
  valid_to          timestamp with time zone,
  
  primary key (phone, customer),
  foreign key (phone)             references phone_numbers(id),
  foreign key (customer)          references customer_codes(id),
  foreign key (phone_number_type) references phone_number_types(id)
);

create table customer_emails(
  email       bigint  not null,
  customer    bigint  not null,
  prim        bool    not null default false,
  email_type  bigint  not null,
  
  valid_from  timestamp with time zone not null default now(),
  valid_to    timestamp with time zone,
  
  primary key (email, customer),
  foreign key (email)       references emails(id),
  foreign key (customer)    references customer_codes(id),
  foreign key (email_type)  references email_types(id)
);

create table accounts(
  id          bigserial not null,
  code        bigint    not null,
  customer    bigint    not null,
  name        text      not null,
  description text,
  
  valid_from  timestamp with time zone not null default now(),
  valid_to    timestamp with time zone,
  
  unique (customer, name),
  primary key (id),
  foreign key (code)      references account_codes(id),
  foreign key (customer)  references customer_codes(id)
);

create table account_addresses(
  address       bigint  not null,
  account       bigint  not null,
  address_type  bigint  not null,
  
  valid_from    timestamp with time zone not null default now(),
  valid_to      timestamp with time zone,
  
  primary key (address, account),
  foreign key (address)       references addresses(id),
  foreign key (account)       references account_codes(id),
  foreign key (address_type)  references address_types(id)
);

create table account_phone_numbers(
  phone             bigint  not null,
  account           bigint  not null,
  phone_number_type bigint  not null,
  
  valid_from        timestamp with time zone not null default now(),
  valid_to          timestamp with time zone,
  
  primary key (phone, account),
  foreign key (phone)             references phone_numbers(id),
  foreign key (account)           references account_codes(id),
  foreign key (phone_number_type) references phone_number_types(id)
);

create table account_emails(
  email       bigint  not null,
  account     bigint  not null,
  email_type  bigint  not null,
  
  valid_from  timestamp with time zone not null default now(),
  valid_to    timestamp with time zone,
  
  primary key (email, account),
  foreign key (email)       references emails(id),
  foreign key (account)     references account_codes(id),
  foreign key (email_type)  references email_types(id)
);

create table contacts(
  id          bigserial not null,
  code        bigint    not null,
  full_name   text      not null,
  description text,
  
  valid_from  timestamp with time zone not null default now(),
  valid_to    timestamp with time zone,
  
  primary key (id),
  foreign key (code)  references contact_codes(id)
);

create table contact_addresses(
  address       bigint  not null,
  contact       bigint  not null,
  address_type  bigint  not null,
  
  valid_from    timestamp with time zone not null default now(),
  valid_to      timestamp with time zone,
  
  primary key (address, contact),
  foreign key (address)       references addresses(id),
  foreign key (contact)       references contact_codes(id),
  foreign key (address_type)  references address_types(id)
);

create table contact_phone_numbers(
  phone             bigint  not null,
  contact           bigint  not null,
  phone_number_type bigint  not null,
  prim              boolean not null,
  
  valid_from        timestamp with time zone not null default now(),
  valid_to          timestamp with time zone,
  
  primary key (phone, contact),
  foreign key (phone)             references phone_numbers(id),
  foreign key (contact)           references contact_codes(id),
  foreign key (phone_number_type) references phone_number_types(id)
);

create table contact_emails(
  email       bigint  not null,
  contact     bigint  not null,
  email_type  bigint  not null,
  prim        boolean not null,
  
  valid_from  timestamp with time zone not null default now(),
  valid_to    timestamp with time zone,
  
  primary key (email, contact),
  foreign key (email)       references emails(id),
  foreign key (contact)     references contact_codes(id),
  foreign key (email_type)  references email_types(id)
);

create table customer_contacts(
  contact       bigint  not null,
  customer      bigint  not null,
  contact_type  bigint  not null,
  
  valid_from    timestamp with time zone not null default now(),
  valid_to      timestamp with time zone,
  
  primary key (contact, customer),
  foreign key (contact)       references contact_codes(id),
  foreign key (customer)      references customer_codes(id),
  foreign key (contact_type)  references contact_types(id)
);

create table account_contacts(
  contact       bigint  not null,
  account       bigint  not null,
  contact_type  bigint  not null,
  
  valid_from    timestamp with time zone not null default now(),
  valid_to      timestamp with time zone,
  
  primary key (contact, account),
  foreign key (contact)       references contact_codes(id),
  foreign key (account)       references account_codes(id),
  foreign key (contact_type)  references contact_types(id)
);

create table meetings(
  id                bigserial                 not null,
  code              bigint                    not null,
  name              text                      not null,
  date              timestamp with time zone  not null,
  planned_duration  bigint                    not null,
  actual_duration   bigint                    not null,
  organizer         text                      not null,
  meeting_type      bigint                    not null,
  description       text,
  
  valid_from        timestamp with time zone not null default now(),
  valid_to          timestamp with time zone,
  
  primary key (id),
  foreign key (code)          references meeting_codes(id),
  foreign key (meeting_type)  references meeting_types(id)
);

create table meeting_notes(
  id          bigserial,
  meeting     bigint    not null,
  note        text      not null,
  
  valid_from  timestamp with time zone not null default now(),
  valid_to    timestamp with time zone,
  
  primary key (id, meeting),
  foreign key (meeting) references meeting_codes(id)
);

create table customer_meeting_participants(
  meeting       bigint  not null,
  customer      bigint  not null,
  
  primary key (meeting, customer),
  foreign key (meeting)       references meeting_codes(id),
  foreign key (customer)      references customer_codes(id)
);

create table app_users(
  id        bigserial,
  username  text      not null unique,
  name      text,
  email     bigint,
  
  primary key (id),
  foreign key (email) references emails(id)
);

create table app_user_phone_numbers(
  phone             bigint  not null,
  app_user          bigint  not null,
  phone_number_type bigint  not null,
  primary_phone     boolean,
  
  valid_from        timestamp with time zone not null default now(),
  valid_to          timestamp with time zone,
  
  primary key (phone, app_user),
  foreign key (phone)             references phone_numbers(id),
  foreign key (app_user)          references app_users(id),
  foreign key (phone_number_type) references phone_number_types(id)
);

create table user_emails(
  email         bigint  not null,
  app_user      bigint  not null,
  email_type    bigint  not null,
  primary_email boolean,
  
  valid_from  timestamp with time zone not null default now(),
  valid_to    timestamp with time zone,
  
  primary key (email, app_user),
  foreign key (email)       references emails(id),
  foreign key (app_user)    references app_users(id),
  foreign key (email_type)  references email_types(id)
);

create table app_roles(
  id    bigserial,
  slug  text      not null unique,
  name  text      not null unique,
  
  primary key (id)
);

create table app_user_roles(
  app_user  bigint,
  app_role  bigint,
  
  primary key (app_user, app_role),
  foreign key (app_user)  references app_users(id),
  foreign key (app_role)  references app_roles(id)
);

create table app_scopes(
  id    bigserial,
  slug  text      not null unique,
  name  text      not null unique,
  
  primary key (id)
);

create table app_user_scope_permissions(
  app_user  bigint,
  app_scope bigint,
  p_access  boolean not null,
  p_create  boolean,
  p_read    boolean,
  p_edit    boolean,
  p_delete  boolean,
  
  primary key (app_user, app_scope),
  foreign key (app_user)  references app_users(id),
  foreign key (app_scope) references app_scopes(id)
);

create table app_role_scope_permissions(
  app_role  bigint,
  app_scope bigint,
  p_access  boolean not null,
  p_create  boolean,
  p_read    boolean,
  p_edit    boolean,
  p_delete  boolean,
  
  primary key (app_role, app_scope),
  foreign key (app_role)  references app_roles(id),
  foreign key (app_scope) references app_scopes(id)
);

create table departments(
  id  bigserial
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

create function generate_random_string(size int, dictionary char[]) returns text as $$
declare
  counter int;
  result text;
begin
  for counter in 1..size
  loop
    result = concat(result, dictionary[floor(random() * array_length(dictionary, 1) + 1)]);
  end loop;

  return result;
end;
$$ LANGUAGE plpgsql;

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

create function customer_codes_gen() returns trigger as $$
declare
  chars char[];
  finished bool;
  result text;
begin
  finished := false;
	while not finished loop
	  result := generate_random_string(8, array['0','1','2','3','4','5','6','7','8','9']);
    finished := not exists(select 1 from customer_codes where code = result);
	end loop;
	new.code := result;
  return NEW;
end;
$$ LANGUAGE plpgsql;
create trigger generate_customer_codes before insert on customer_codes for each row execute procedure customer_codes_gen();

create function account_codes_gen() returns trigger as $$
declare
  chars char[];
  finished bool;
  customer_code text;
  result text;
begin
  finished := false;
  select code into customer_code from customer_codes where id = new.customer;
  while not finished loop
    result := customer_code || '-' || generate_random_string(4, array['0','1','2','3','4','5','6','7','8','9']);
    finished := not exists(select 1 from account_codes where code = result);
  end loop;
  new.code := result;
  return NEW;
end;
$$ LANGUAGE plpgsql;
create trigger generate_account_codes before insert on account_codes for each row execute procedure account_codes_gen();

create function contact_codes_gen() returns trigger as $$
declare
  chars char[];
  finished bool;
  result text;
begin
  finished := false;
  while not finished loop
    result := generate_random_string(8, array['0','1','2','3','4','5','6','7','8','9']);
    finished := not exists(select 1 from contact_codes where code = result);
  end loop;
  new.code := result;
  return NEW;
end;
$$ LANGUAGE plpgsql;
create trigger generate_contact_codes before insert on contact_codes for each row execute procedure contact_codes_gen();