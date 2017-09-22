drop schema if exists primrose cascade;
create schema primrose;
/*
 * 
 * TABLES
 * 
 */
create table principals(
  id      bigint  constraint  nn_users_id       not null,
  name    text    constraint  nn_users_name     not null constraint uq_principals_name unique,
  enabled bool    constraint  nn_users_enabled  not null,
  locked  bool    constraint  nn_users_locked   not null,
  
  created_by  bigint                    constraint  nn_users_created_by       not null,
  created_at  timestamp with time zone  constraint  nn_user_users_valid_from  not null  default current_timestamp,
  edited_by   bigint,
  edited_at   timestamp with time zone,
  
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
  edited_at   timestamp with time zone,
  
  constraint pk_credidentials             primary key (id),
  constraint fk_credidentials_principal   foreign key (principal)   references principals(id),
  constraint fk_credidentials_created_by  foreign key (created_by)  references principals(id),
  constraint fk_credidentials_edited_by   foreign key (edited_by)   references principals(id)
);
comment on table credidentials is 'User credidentials';

create table principal_groups(
  id      bigint  constraint nn_principal_groups_id   not null,
  name    text    constraint nn_principal_groups_name not null constraint uq_principal_groups_name unique,
  parent  bigint,
  
  constraint pk_principal_groups        primary key (id),
  constraint fk_principal_groups_parent foreign key (parent)  references principal_groups(id)
);
comment on table principal_groups is 'Groups';

create table principal_roles(
  id      bigint  constraint nn_roles_id    not null,
  name    text    constraint nn_roles_name  not null constraint uq_principal_roles_name unique,
  
  constraint pk_principal_roles primary key (id)
);
comment on table principal_roles is 'Roles';

create table permissions(
  id    bigint  constraint nn_permissions_id    not null,
  name  text    constraint nn_permissions_name  not null constraint uq_permissions_name unique,
  
  constraint pk_permissions primary key (id)
);
comment on table permissions is 'Permissions';

create table principal_group_memberships(
  principal_group bigint  constraint nn_principal_group_memberships_principal_group  not null,
  principal       bigint  constraint nn_principal_group_memberships_principal        not null,
  
  constraint pk_principal_group_memberships                 primary key (principal_group, principal),
  constraint fk_principal_group_memberships_principal_group foreign key (principal_group) references principal_groups(id),
  constraint fk_principal_group_memberships_principal       foreign key (principal)       references principals(id)
);
comment on table principal_group_memberships is 'Principal group memberships';

create table principal_role_memberships(
  principal_role  bigint  constraint nn_principal_role_memberships_principal_role not null,
  principal       bigint  constraint nn_principal_role_memberships_principal      not null,
  
  constraint pk_principal_role_memberships                  primary key (principal_role, principal),
  constraint fk_principal_role_memberships_principal_group  foreign key (principal_role)  references principal_roles(id),
  constraint fk_principal_role_memberships_principal        foreign key (principal)       references principals(id)
);
comment on table principal_role_memberships is 'Principal role memberhips';

create table group_role_memberships(
  principal_group bigint  constraint nn_group_role_memberships_principal_group not null,
  principal_role  bigint  constraint nn_group_role_memberships_principal_role  not null,
  
  constraint pk_group_role_memberships                  primary key (principal_group, principal_role),
  constraint fk_group_role_memberships_principal_group  foreign key (principal_group) references principal_groups(id),
  constraint fk_group_role_memberships_principal_role   foreign key (principal_role)  references principal_roles(id)
);
comment on table group_role_memberships is 'Group role membership';

create table role_permissions(
  permission      bigint  constraint nn_group_role_memberships_permission     not null,
  principal_role  bigint  constraint nn_group_role_memberships_principal_role not null,
  
  constraint pk_role_permissions                primary key (permission, principal_role),
  constraint fk_role_permissions_permission     foreign key (permission)      references permissions(id),
  constraint fk_role_permissions_principal_role foreign key (principal_role)  references principal_roles(id)
);
comment on table role_permissions is 'Eole permissions';

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
create or replace function pseudo_encrypt(value int) returns int as $$
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

create or replace function pseudo_encrypt(value bigint) returns bigint as $$
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
        r2 := l1 # ((((1366.0 * r1 + 150889) % 714025) / 714025.0) * 32767*32767)::int;
        l1 := l2;
        r1 := r2;
        i := i + 1;
    end loop;
return ((l1::bigint << 32) + r1);
end;
$$ language plpgsql;

create or replace function stringify_bigint(n bigint) returns text as $$
declare
 alphabet text:='abcdefghijklmnopqrstuvwxyz0123456789';
 base int:=length(alphabet); 
 _n bigint:=abs(n);
 output text:='';
begin
 loop
   output := output || substr(alphabet, 1+(_n%base)::int, 1);
   _n := _n / base; 
   exit when _n=0;
 end loop;
 return output;
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

create trigger account_code           before  insert on t_accounts        for each row execute procedure unique_code();
create trigger address_code           before  insert on t_addresses       for each row execute procedure unique_code();
create trigger contact_code           before  insert on t_contacts        for each row execute procedure unique_code();

/*
 * 
 * DATA
 * 
 */
insert into permissions(id, name) values 
(1, 'account_read'),
(2, 'account_write'),
(3, 'account_delete');

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