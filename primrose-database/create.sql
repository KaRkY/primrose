drop schema if exists primrose cascade;
create schema primrose;

-- TODO remove validity and create history tables

/*
 * 
 * ENUM TABLES
 * 
 */

create table customer_types(
  id            bigserial not null,
  code          text      not null  unique,
  default_name  text      not null  unique,
  sort          int       not null  unique,
  
  constraint pk_customer_types              primary key (id),
  constraint uq_customer_types_code         unique (code),
  constraint uq_customer_types_default_name unique (default_name),
  constraint uq_customer_types_sort         unique (sort)
);

insert into customer_types(code, default_name, sort) values 
('person', 'Person', 1),
('company', 'Company', 2);

create table address_types(
  id            bigserial not null,
  code          text      not null  unique,
  default_name  text      not null  unique,
  sort          int       not null  unique,
  
  constraint pk_address_types               primary key (id),
  constraint uq_address_types_code          unique (code),
  constraint uq_address_types_default_name  unique (default_name),
  constraint uq_address_types_sort          unique (sort)
);

insert into address_types(code, default_name, sort) values
('billing', 'Billing', 1),
('shipping', 'Shipping', 2);

create table customer_relation_types(
  id            bigserial not null,
  code          text      not null  unique,
  default_name  text      not null  unique,
  sort          int       not null  unique,
  
  constraint pk_customer_relation_types               primary key (id),
  constraint uq_customer_relation_types_code          unique (code),
  constraint uq_customer_relation_types_default_name  unique (default_name),
  constraint uq_customer_relation_types_sort          unique (sort)
);

insert into customer_relation_types(code, default_name, sort) values 
('customer', 'Customer', 1),
('partner', 'Partner', 2),
('investor', 'Investor', 3),
('reseller', 'Reseller', 4);

create table phone_number_types(
  id            bigserial not null,
  code          text      not null  unique,
  default_name  text      not null  unique,
  sort          int       not null  unique,
  
  constraint pk_phone_number_types                primary key (id),
  constraint uq_phone_number_types_code           unique (code),
  constraint uq_phone_number_types_default_name   unique (default_name),
  constraint uq_phone_number_types_sort           unique (sort)
);

insert into phone_number_types(code, default_name, sort) values 
('work', 'Work', 1),
('home', 'Home', 2),
('other', 'Other', 3);

create table email_types(
  id            bigserial not null,
  code          text      not null  unique,
  default_name  text      not null  unique,
  sort          int       not null  unique,
  
  constraint pk_email_types               primary key (id),
  constraint uq_email_types_code          unique (code),
  constraint uq_email_types_default_name  unique (default_name),
  constraint uq_email_types_sort          unique (sort)
);

insert into email_types(code, default_name, sort) values 
('work', 'Work', 1),
('home', 'Home', 2),
('other', 'Other', 3);

create table contact_types(
  id            bigserial not null,
  code          text      not null  unique,
  default_name  text      not null  unique,
  sort          int       not null  unique,
  
  constraint pk_contact_types               primary key (id),
  constraint uq_contact_types_code          unique (code),
  constraint uq_contact_types_default_name  unique (default_name),
  constraint uq_contact_types_sort          unique (sort)
);

insert into contact_types(code, default_name, sort) values 
('sales', 'Sales', 1),
('manager', 'Manager', 2);

create table meeting_types(
  id            bigserial not null,
  code          text      not null  unique,
  default_name  text      not null  unique,
  sort          int       not null  unique,
  
  constraint pk_meeting_types               primary key (id),
  constraint uq_meeting_types_code          unique (code),
  constraint uq_meeting_types_default_name  unique (default_name),
  constraint uq_meeting_types_sort          unique (sort)
);

insert into meeting_types(code, default_name, sort) values 
('call', 'Call', 1),
('on-site', 'On Site', 2),
('email', 'Email', 3),
('mail', 'mail', 4);

/*
 * 
 * TABLES
 * 
 */
create table customers(
  id    bigserial not null,
  code  text      not null,
  customer_type           bigint      not null,
  customer_relation_type  bigint      not null,
  full_name               text        not null,
  display_name            text,
  description             text,
  created_by              text        not null,
  changed_by              text        not null,
  created_at              timestamptz not null,
  changed_at              timestamptz not null,
  
  constraint pk_customers                             primary key (id),
  constraint fk_customer_data_customer_type           foreign key (customer_type)           references customer_types(id),
  constraint fk_customer_data_customer_relation_type  foreign key (customer_relation_type)  references customer_relation_types(id),
  constraint uq_customers_code                        unique (code)
);

create table customer_phone_numbers(
  customer          bigint  not null,
  phone_number_type bigint  not null,
  phone_number      text    not null,
  
  constraint pk_customer_phone_numbers                    primary key (customer, phone_number),
  constraint fk_customer_phone_numbers_customer           foreign key (customer)          references customers(id),
  constraint fk_customer_phone_numbers_phone_number_type  foreign key (phone_number_type) references phone_number_types(id)
);

create table customer_emails(
  customer    bigint  not null,
  email_type  bigint  not null,
  email       text    not null,
  
  constraint pk_customer_emails             primary key (customer, email),
  constraint fk_customer_emails_customer    foreign key (customer)    references customers(id),
  constraint fk_customer_emails_email_type  foreign key (email_type)  references email_types(id)
);

create table contacts(
  id          bigserial   not null,
  code        text        not null,
  full_name   text        not null,
  description text,
  created_by  text        not null,
  changed_by  text        not null,
  created_at  timestamptz not null,
  changed_at  timestamptz not null,

  constraint pk_contacts      primary key (id),
  constraint uq_contacts_code unique(code)
);

create table contact_phone_numbers(
  contact           bigint  not null,
  phone_number_type bigint  not null,
  phone_number      text    not null,
  
  constraint pk_contact_phone_numbers                   primary key (contact, phone_number),
  constraint fk_contact_phone_numbers_contact           foreign key (contact)           references contacts(id),
  constraint fk_contact_phone_numbers_phone_number_type foreign key (phone_number_type) references phone_number_types(id)
);

create table contact_emails(
  contact     bigint  not null,
  email_type  bigint  not null,
  email       text    not null,
  
  constraint pk_contact_emails            primary key (contact, email),
  constraint fk_contact_emails_contact    foreign key (contact)     references contacts(id),
  constraint fk_contact_emails_email_type foreign key (email_type)  references email_types(id)
);

create table customer_contacts(
  customer    bigint      not null,
  contact     bigint      not null,
  
  constraint pk_customer_contacts           primary key (customer, contact),
  constraint fk_customer_contacts_contact   foreign key (contact)   references contacts(id),
  constraint fk_customer_contacts_customer  foreign key (customer)  references customers(id)
);

create table meetings(
  id    bigserial not null,
  code  text      not null,
  
  constraint pk_meetings      primary key (id),
  constraint uq_meetings_code unique (code)
);

create table meeting_data(
  meeting           bigint      not null,                   
  name              text        not null,
  start_date        timestamptz not null,
  planned_duration  bigint      not null,
  actual_duration   bigint      not null,
  organizer         text        not null,
  meeting_type      bigint      not null,
  description       text,
  valid_from        timestamptz not null default now(),
  valid_to          timestamptz,
  
  constraint pk_meeting_data              primary key (meeting, valid_from),
  constraint fk_meeting_data_meeting      foreign key (meeting)       references meetings(id),
  constraint fk_meeting_data_meeting_type foreign key (meeting_type)  references meeting_types(id),
  constraint rg_meeting_data_validity     exclude using gist (
    meeting with =,
    tstzrange(valid_from, coalesce(valid_to, 'infinity'), '[)') with &&
  )
);

create table notes(
  id    bigserial not null,
  code  text      not null,
  
  constraint pk_notes       primary key (id),
  constraint uq_notes_code  unique(code)
);

create table meeting_notes(
  meeting     bigint      not null,
  note        bigint      not null,
  valid_from  timestamptz not null default now(),
  valid_to    timestamptz,
  
  constraint pk_meeting_notes           primary key (meeting, note, valid_from),
  constraint fk_meeting_notes_meeting   foreign key (meeting) references meetings(id),
  constraint fk_meeting_notes_note      foreign key (note)    references notes(id),
  constraint rg_meeting_notes_validity  exclude using gist (
    meeting with =,
    note with =,
    tstzrange(valid_from, coalesce(valid_to, 'infinity'), '[)') with &&
  )
);

create table meeting_customer_participants(
  meeting     bigint      not null,
  customer    bigint      not null,
  valid_from  timestamptz not null default now(),
  valid_to    timestamptz,
  
  constraint pk_meeting_customer_participants           primary key (meeting, customer, valid_from),
  constraint fk_meeting_customer_participants_meeting   foreign key (meeting)   references meetings(id),
  constraint fk_meeting_customer_participants_customer  foreign key (customer)  references customers(id),
  constraint rg_meeting_customer_participants_validity  exclude using gist (
    meeting with =,
    customer with =,
    tstzrange(valid_from, coalesce(valid_to, 'infinity'), '[)') with &&
  )
);

create table app_users(
  id        bigserial,
  username  text      not null unique,
  name      text,
  email     text,
  
  primary key (id)
);

create table app_user_phone_numbers(
  id                bigserial not null,
  app_user          bigint    not null,
  phone             text      not null,
  phone_number_type bigint    not null,
  primary_phone     boolean,
  
  primary key (id, app_user),
  foreign key (app_user)          references app_users(id),
  foreign key (phone_number_type) references phone_number_types(id)
);

create table app_user_emails(
  id            bigserial not null,
  app_user      bigint    not null,
  email         text      not null,
  email_type    bigint    not null,
  primary_email boolean,
  
  primary key (id, app_user),
  foreign key (app_user)    references app_users(id),
  foreign key (email_type)  references email_types(id)
);

create table app_roles(
  id    bigserial,
  code  text      not null unique,
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
  code  text      not null unique,
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
    finished := not exists(select 1 from customers where code = result);
	end loop;
	new.code := result;
  return NEW;
end;
$$ LANGUAGE plpgsql;
create trigger generate_customer_codes before insert on customers for each row execute procedure customer_codes_gen();

create function contact_codes_gen() returns trigger as $$
declare
  chars char[];
  finished bool;
  result text;
begin
  finished := false;
  while not finished loop
    result := generate_random_string(8, array['0','1','2','3','4','5','6','7','8','9']);
    finished := not exists(select 1 from contacts where code = result);
  end loop;
  new.code := result;
  return NEW;
end;
$$ LANGUAGE plpgsql;
create trigger generate_contact_codes before insert on contacts for each row execute procedure contact_codes_gen();