/*
 * 
 * TABLES
 * 
 */
create table principals(
  id      bigint  constraint  nn_users_id       not null,
  code    text    constraint  nn_users_code     not null,
  name    text    constraint  nn_users_name     not null,
  enabled bool    constraint  nn_users_enabled  not null,
  locked  bool    constraint  nn_users_locked   not null,
  
  created_by  bigint                    constraint  nn_users_created_by       not null,
  created_at  timestamp with time zone  constraint  nn_user_users_valid_from  not null  default current_timestamp,
  edited_by   bigint,
  created_at  timestamp with time zone,
  
  constraint pk_users                 primary key (id),
  constraint fk_principals_created_by foreign key (created_by)  references principals(id),
  constraint fk_principals_edited_by  foreign key (edited_by)   references principals(id)
);
comment on table principals is 'Principals table for authentication';

create table credidentials(
  id        bigint                    constraint  nn_credidentials_id         not null,
  principal bigint                    constraint  nn_credidentials_principal  not null,
  value     text                      constraint  nn_credidentials_value      not null,
  
  created_by  bigint                    constraint  nn_users_created_by       not null,
  created_at  timestamp with time zone  constraint  nn_user_users_valid_from  not null  default current_timestamp,
  edited_by   bigint,
  created_at  timestamp with time zone,
  
  constraint pk_credidentials             primary key (id)
  constraint fk_credidentials_principal   foreign key (principal)   references principals(id),
  constraint fk_credidentials_created_by  foreign key (created_by)  references principals(id),
  constraint fk_credidentials_edited_by   foreign key (edited_by)   references principals(id)
);
comment on table credidentials is 'User credidentials';

create table groups(
  id      bigint  constraint nn_groups_group_id   not null,
  code    text    constraint nn_groups_group_code not null,
  name    text    constraint nn_groups_group_name not null,
  parent  bigint,
  
  constraint pk_groups        primary key (group_id),
  constraint fk_groups_parent foreign key (parent)  references groups(id)
);
comment on table groups is 'Groups';

create table t_user_groups(
  user_group_user_id  bigint  constraint nn_user_groups_user_group_user_id  not null,
  user_group_group_id bigint  constraint nn_user_groups_user_group_group_id not null,
  
  constraint pk_user_groups                     primary key (user_group_user_id, user_group_group_id),
  constraint fk_user_groups_user_group_user_id  foreign key (user_group_user_id)  references t_users(user_id),
  constraint fk_user_groups_user_group_group_id foreign key (user_group_group_id)  references t_groups(group_id)
);
comment on table t_user_groups is 'User groups';

create table t_permissions(
  permission_id      bigint  constraint nn_permissions_permission_id   not null,
  permission_code    text    constraint nn_permissions_permission_code not null,
  permission_name    text    constraint nn_permissions_permission_name not null,
  permission_parent  bigint,
  
  constraint pk_permissions primary key (permission_id)
);
comment on table t_permissions is 'Permissions';

create table t_group_permissions(
  group_permission_group_id     bigint  constraint nn_group_permissions_group_permission_group_id       not null,
  group_permission_permisson_id bigint  constraint nn_group_permissions_group_permission_permission_id  not null,
  
  constraint pk_group_permissions                               primary key (group_permission_group_id, group_permission_permisson_id),
  constraint fk_group_permissions_group_permission_group_id     foreign key (group_permission_group_id)     references t_groups(group_id),
  constraint fk_group_permissions_group_permission_permisson_id foreign key (group_permission_permisson_id) references t_permissions(permission_id)
);
comment on table t_group_permissions is 'Group permissions';

create table t_account_types(
  account_type_id       bigint  constraint nn_account_types_account_type_id       not null,
  account_type_code     text    constraint nn_account_types_account_type_code     not null,
  account_type_default  text    constraint nn_account_types_account_type_default  not null,
  
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
  
  valid_from        timestamp with time zone  constraint nn_user_usernames_valid_from     not null  default current_timestamp,
  valid_to          timestamp with time zone  constraint nn_user_usernames_valid_to       not null  default 'infinity'::timestamp,
  
  constraint pk_accounts                primary key (account_id),
  constraint ck_accounts_positive_range check (valid_to > valid_from),
  constraint fk_account_parent_account  foreign key (parent_account_id)   references t_accounts(account_id),
  constraint fk_account_type            foreign key (account_type_id)     references t_account_types(account_type_id)
);
comment on table t_accounts is 'Actual account data.';

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

create table t_account_address_types(
  account_address_type_id       bigint  constraint nn_account_address_type_id       not null,
  account_address_type_code     text    constraint nn_account_address_type_code     not null,
  account_address_type_default  text    constraint nn_account_address_type_default  not null,
  
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
  
  constraint pk_contacts  primary key (contact_id)
);
comment on table t_contacts is 'Contact data.';

create table t_account_contact_types(
  account_contact_type_id       bigint  constraint nn_account_contact_types_account_contact_type_id       not null,
  account_contact_type_code     text    constraint nn_account_contact_types_account_contact_type_code     not null,
  account_contact_type_default  text    constraint nn_account_contact_types_account_contact_type_default  not null,
  account_contact_type_parent   bigint,
  
  constraint pk_account_contact_types       primary key (account_contact_type_id),
  constraint fk_account_contact_type_parent foreign key (account_contact_type_parent) references t_account_contact_types(account_contact_type_id)
);
comment on table t_account_contact_types is 'Enumeration of contact types.';

create table t_account_contacts(
  account_id              bigint  constraint nn_account_contacts_account_id   not null,
  contact_id              bigint  constraint nn_account_contacts_contact_id   not null,
  account_contact_type_id bigint,
  
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

/*
 * 
 * SEQUENCES
 * 
 */
create sequence s_user          start with 1  increment by 1;
create sequence s_group         start with 1  increment by 1;
create sequence s_permission    start with 1  increment by 1;
create sequence s_user_username start with 1  increment by 1;
create sequence s_user_password start with 1  increment by 1;
create sequence s_account       start with 1  increment by 1;
create sequence s_address       start with 1  increment by 1;
create sequence s_contact       start with 1  increment by 1;
create sequence s_contact_types start with 1  increment by 1;

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

create function user_unique_code() returns trigger as $$
declare
  key text;
  qry text;
  found text;
  min_size int;
begin
  min_size = 6;
  
  loop
    key := generate_random_string_base36(min_size);
    
    if not exists( select 1 from t_users where user_code = key) then
      exit;
    end if;
    
    min_size = min_size + 1;
  end loop;

  new.user_code = key;

  return new;
end;
$$ LANGUAGE plpgsql;

create function group_unique_code() returns trigger as $$
declare
  key text;
  qry text;
  found text;
  min_size int;
begin
  min_size = 6;
  
  loop
    key := generate_random_string_base36(min_size);
    
    if not exists( select 1 from t_groups where group_code = key) then
      exit;
    end if;
    
    min_size = min_size + 1;
  end loop;

  new.group_code = key;

  return new;
end;
$$ LANGUAGE plpgsql;

create function permission_unique_code() returns trigger as $$
declare
  key text;
  qry text;
  found text;
  min_size int;
begin
  min_size = 6;
  
  loop
    key := generate_random_string_base36(min_size);
    
    if not exists( select 1 from t_permissions where permission_code = key) then
      exit;
    end if;
    
    min_size = min_size + 1;
  end loop;

  new.permission_code = key;

  return new;
end;
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

create trigger user_code        before  insert on t_users       for each row execute procedure user_unique_code();
create trigger group_code       before  insert on t_groups      for each row execute procedure group_unique_code();
create trigger permission_code  before  insert on t_permissions for each row execute procedure permission_unique_code();
create trigger account_code     before  insert on t_accounts    for each row execute procedure account_unique_code();
create trigger address_code     before  insert on t_addresses   for each row execute procedure address_unique_code();
create trigger contact_code     before  insert on t_contacts    for each row execute procedure contact_unique_code();

/*
 * 
 * DATA
 * 
 */
insert into t_permissions(permission_id, permission_name) values
(nextval('s_permission'), 'account_view'),
(nextval('s_permission'), 'account_create'),
(nextval('s_permission'), 'account_edit'),
(nextval('s_permission'), 'account_delete'),
(nextval('s_permission'), 'contact_view'),
(nextval('s_permission'), 'contact_create'),
(nextval('s_permission'), 'contact_edit'),
(nextval('s_permission'), 'contact_delete');

insert into t_groups(group_id, group_name, group_parent) values
(nextval('s_group'), 'admin', null);

insert into t_group_permissions(group_permission_group_id, group_permission_permisson_id)
(select currval('s_group'), permission_id from t_permissions);

insert into t_users(user_id, user_name, user_locked, user_enabled) values
(nextval('s_user'), 'Admin', false, true);

insert into t_user_groups(user_group_group_id, user_group_user_id) values
(currval('s_group'), currval('s_user'));

insert into t_user_usernames(user_username_id, user_user_id, user_username) values
(nextval('s_user_username'), currval('s_user'), 'admin');

insert into t_user_passwords(user_password_id, user_user_id, user_password) values
(nextval('s_user_password'), currval('s_user'), '$2a$10$0JKQjjbqE8uriuKuV7WvOefW3h.ZfhhEu9hr1G0ZSDwyR91ahNEOi');

insert into t_account_types(account_type_id, account_type_code, account_type_default) values 
(1, 'customer', 'Customer'),
(2, 'partner',  'Partner'),
(3, 'investor', 'Investor'),
(4, 'reseller', 'Reseller');

insert into t_account_address_types(account_address_type_id, account_address_type_code, account_address_type_default) values
(1, 'billing',  'Billing'),
(2, 'shipping', 'Shipping');

insert into t_account_contact_types(account_contact_type_id, account_contact_type_parent, account_contact_type_code, account_contact_type_default) values
(1, null, 'seller',     'Seller'),
(2, null, 'manager',    'Manager'),
(5, null, 'developer',  'Developer'),
(6, null, 'consultant', 'Consultant');