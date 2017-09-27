drop schema if exists primrose cascade;
create schema primrose;
/*
 * 
 * TABLES
 * 
 */
create table principals(
  id          bigint                    constraint  nn_principals_id          not null,
  name        text                      constraint  nn_principals_name        not null  constraint uq_principals_name unique,
  enabled     bool                      constraint  nn_principals_enabled     not null,
  locked      bool                      constraint  nn_principals_locked      not null,
  created_by  bigint                    constraint  nn_principals_created_by  not null,
  created_at  timestamp with time zone  constraint  nn_principalscreated_at   not null  default current_timestamp,
  edited_by   bigint,
  edited_at   timestamp with time zone,
  
  constraint pk_principals            primary key (id),
  constraint fk_principals_created_by foreign key (created_by)  references principals(id),
  constraint fk_principals_edited_by  foreign key (edited_by)   references principals(id)
);
comment on table principals is 'Principals table for authentication';

create table credidentials(
  id          bigint                    constraint  nn_credidentials_id         not null,
  principal   bigint                    constraint  nn_credidentials_principal  not null,
  value       text                      constraint  nn_credidentials_value      not null,
  valid_from  timestamp with time zone  constraint  nn_credidentials_valid_from not null  default current_timestamp,
  valid_to    timestamp with time zone  constraint  nn_credidentials_valid_to   not null  default 'infinity'::timestamp,
  created_by  bigint                    constraint  nn_credidentials_created_by not null,
  created_at  timestamp with time zone  constraint  nn_credidentials_created_at not null  default current_timestamp,
  edited_by   bigint,
  edited_at   timestamp with time zone,
  
  constraint pk_credidentials             primary key (id),
  constraint fk_credidentials_created_by  foreign key (created_by)  references principals(id),
  constraint fk_credidentials_edited_by   foreign key (edited_by)   references principals(id)
);
comment on table credidentials is 'User credidentials';

create table principal_groups(
  id          bigint                    constraint  nn_principal_groups_id          not null,
  name        text                      constraint  nn_principal_groups_name        not null  constraint uq_principal_groups_name unique,
  parent      bigint,
  created_by  bigint                    constraint  nn_principal_groups_created_by  not null,
  created_at  timestamp with time zone  constraint  nn_principal_groups_created_at  not null  default current_timestamp,
  edited_by   bigint,
  edited_at   timestamp with time zone,
  
  constraint pk_principal_groups            primary key (id),
  constraint fk_principal_groups_parent     foreign key (parent)      references principal_groups(id),
  constraint fk_principal_groups_created_by foreign key (created_by)  references principals(id),
  constraint fk_principal_groups_edited_by  foreign key (edited_by)   references principals(id)
);
comment on table principal_groups is 'Groups';

create table principal_roles(
  id          bigint                    constraint  nn_principal_roles_id         not null,
  name        text                      constraint  nn_principal_roles_name       not null  constraint uq_principal_roles_name  unique,
  created_by  bigint                    constraint  nn_principal_roles_created_by not null,
  created_at  timestamp with time zone  constraint  nn_principal_roles_created_at not null  default current_timestamp,
  edited_by   bigint,
  edited_at   timestamp with time zone,
  
  constraint pk_principal_roles             primary key (id),
  constraint fk_principal_roles_created_by  foreign key (created_by)  references principals(id),
  constraint fk_principal_roles_edited_by   foreign key (edited_by)   references principals(id)
);
comment on table principal_roles is 'Roles';

create table resources(
  id    bigint  constraint nn_resources_id    not null,
  name  text    constraint nn_resources_name  not null constraint uq_resources_name unique,
  
  constraint pk_resources primary key (id)
);
comment on table resources is 'Resources';

create table operations(
  id    bigint  constraint nn_operations_id   not null,
  name  text    constraint nn_operations_name not null constraint uq_operations_name unique,
  
  constraint pk_operations  primary key (id)
);
comment on table operations is 'Operations';

create table principal_group_memberships(
  principal_group bigint                    constraint  nn_principal_group_memberships_principal_group  not null,
  principal       bigint                    constraint  nn_principal_group_memberships_principal        not null,
  created_by      bigint                    constraint  nn_principal_group_memberships_created_by       not null,
  created_at      timestamp with time zone  constraint  nn_principal_group_memberships_created_at       not null  default current_timestamp,
  edited_by       bigint,
  edited_at       timestamp with time zone,
  
  constraint pk_principal_group_memberships                 primary key (principal_group, principal),
  constraint fk_principal_group_memberships_principal_group foreign key (principal_group) references principal_groups(id),
  constraint fk_principal_group_memberships_principal       foreign key (principal)       references principals(id),
  constraint fk_principal_group_memberships_created_by      foreign key (created_by)      references principals(id),
  constraint fk_principal_group_memberships_edited_by       foreign key (edited_by)       references principals(id)
);
comment on table principal_group_memberships is 'Principal group memberships';

create table principal_role_memberships(
  principal_role  bigint                    constraint  nn_principal_role_memberships_principal_role  not null,
  principal       bigint                    constraint  nn_principal_role_memberships_principal       not null,
  created_by      bigint                    constraint  nn_principal_role_memberships_created_by      not null,
  created_at      timestamp with time zone  constraint  nn_principal_role_memberships_created_at      not null  default current_timestamp,
  edited_by       bigint,
  edited_at       timestamp with time zone,
  
  constraint pk_principal_role_memberships                  primary key (principal_role, principal),
  constraint fk_principal_role_memberships_principal_group  foreign key (principal_role)  references principal_roles(id),
  constraint fk_principal_role_memberships_principal        foreign key (principal)       references principals(id),
  constraint fk_principal_role_memberships_created_by       foreign key (created_by)      references principals(id),
  constraint fk_principal_role_memberships_edited_by        foreign key (edited_by)       references principals(id)
);
comment on table principal_role_memberships is 'Principal role memberhips';

create table group_role_memberships(
  principal_group bigint                    constraint  nn_group_role_memberships_principal_group not null,
  principal_role  bigint                    constraint  nn_group_role_memberships_principal_role  not null,
  created_by      bigint                    constraint  nn_group_role_memberships_created_by      not null,
  created_at      timestamp with time zone  constraint  nn_group_role_memberships_created_at      not null  default current_timestamp,
  edited_by       bigint,
  edited_at       timestamp with time zone,
  
  constraint pk_group_role_memberships                  primary key (principal_group, principal_role),
  constraint fk_group_role_memberships_principal_group  foreign key (principal_group) references principal_groups(id),
  constraint fk_group_role_memberships_principal_role   foreign key (principal_role)  references principal_roles(id),
  constraint fk_group_role_memberships_created_by       foreign key (created_by)      references principals(id),
  constraint fk_group_role_memberships_edited_by        foreign key (edited_by)       references principals(id)
);
comment on table group_role_memberships is 'Group role membership';

create table role_permissions(
  principal_role  bigint                    constraint  nn_group_role_memberships_principal_role not null,
  resource        bigint                    constraint  nn_group_role_memberships_resource       not null,
  operation       bigint                    constraint  nn_group_role_memberships_operation      not null,
  created_by      bigint                    constraint  nn_group_role_memberships_created_by     not null,
  created_at      timestamp with time zone  constraint  nn_group_role_memberships_created_at     not null  default current_timestamp,
  edited_by       bigint,
  edited_at       timestamp with time zone,
  
  constraint pk_role_permissions                primary key (principal_role, resource, operation),
  constraint fk_role_permissions_principal_role foreign key (principal_role)  references principal_roles(id),
  constraint fk_role_permissions_resource       foreign key (resource)        references resources(id),
  constraint fk_role_permissions_operation      foreign key (operation)       references operations(id),
  constraint fk_role_permissions_created_by     foreign key (created_by)      references principals(id),
  constraint fk_role_permissions_edited_by      foreign key (edited_by)       references principals(id)
);
comment on table role_permissions is 'Role permissions';

create table account_types(
  id    bigint                          constraint  nn_account_types_id         not null,
  name  text                            constraint  nn_account_types_name       not null,
  created_by  bigint                    constraint  nn_account_types_created_by not null,
  created_at  timestamp with time zone  constraint  nn_account_types_created_at not null  default current_timestamp,
  edited_by   bigint,
  edited_at   timestamp with time zone,
  
  constraint pk_account_types             primary key (id),
  constraint fk_account_types_created_by  foreign key (created_by)  references principals(id),
  constraint fk_account_types_edited_by   foreign key (edited_by)   references principals(id)
);
comment on table account_types is 'Enumeration of diferent account types.';

create table accounts(
  id              bigint                    constraint nn_account_id            not null,
  account_type    bigint                    constraint nn_account_type          not null,
  parent_account  bigint,
  name            text                      constraint nn_account_name          not null  constraint uq_account_name  unique,
  display_name    text,
  email           text,
  phone           text,
  website         text,
  description     text,
  valid_from      timestamp with time zone  constraint  nn_accounts_valid_from  not null  default current_timestamp,
  valid_to        timestamp with time zone  constraint  nn_accounts_valid_to    not null  default 'infinity'::timestamp,
  created_by      bigint                    constraint  nn_accounts_created_by  not null,
  created_at      timestamp with time zone  constraint  nn_accounts_created_at  not null  default current_timestamp,
  edited_by       bigint,
  edited_at       timestamp with time zone,
  
  constraint pk_accounts                  primary key (id),
  constraint ck_accounts_positive_range   check (valid_to > valid_from),
  constraint fk_accounts_parent_account   foreign key (parent_account)  references accounts(id),
  constraint fk_accounts_type             foreign key (account_type)    references account_types(id),
  constraint fk_accounts_type_created_by  foreign key (created_by)      references principals(id),
  constraint fk_accounts_type_edited_by   foreign key (edited_by)       references principals(id)
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
  created_by    bigint                    constraint  nn_addresses_created_by     not null,
  created_at    timestamp with time zone  constraint  nn_addresses_created_at     not null  default current_timestamp,
  edited_by     bigint,
  edited_at     timestamp with time zone,
  
  constraint pk_addresses             primary key (id),
  constraint fk_addresses_created_by  foreign key (created_by)  references principals(id),
  constraint fk_addresses_edited_by   foreign key (edited_by)   references principals(id)
);
comment on table addresses is 'Simple address data.';

create table account_address_types(
  id          bigint                    constraint  nn_account_address_types_id         not null,
  name        text                      constraint  nn_account_address_types_name       not null  constraint uq_account_address_types_name  unique,
  created_by  bigint                    constraint  nn_account_address_types_created_by not null,
  created_at  timestamp with time zone  constraint  nn_account_address_types_created_at not null  default current_timestamp,
  edited_by   bigint,
  edited_at   timestamp with time zone,
  
  constraint pk_account_address_types primary key (id),
  constraint fk_addresses_created_by  foreign key (created_by)  references principals(id),
  constraint fk_addresses_edited_by   foreign key (edited_by)   references principals(id)
);
comment on table account_address_types is 'Enumeration of address types.';

create table account_addresses(
  account               bigint                    constraint nn_account_addresses_account               not null,
  address               bigint                    constraint nn_account_addresses_address               not null,
  account_address_type  bigint                    constraint nn_account_addresses_account_address_type  not null,
  created_by            bigint                    constraint nn_account_addresses_created_by            not null,
  created_at            timestamp with time zone  constraint nn_account_addresses_created_at            not null  default current_timestamp,
  edited_by             bigint,
  edited_at             timestamp with time zone,
  
  constraint pk_account_addresses           primary key (account, address),
  constraint fk_account_address_account     foreign key (account)               references accounts(id),
  constraint fk_account_address_address     foreign key (address)               references addresses(id),
  constraint fk_account_address_type        foreign key (account_address_type)  references account_address_types(id),
  constraint fk_account_address_created_by  foreign key (created_by)            references principals(id),
  constraint fk_account_address_edited_by   foreign key (edited_by)             references principals(id)
);
comment on table account_addresses is 'Account address by type.';

create table contacts(
  id          bigint                    constraint  nn_contacts_id          not null,
  name        text                      constraint  nn_contacts_name        not null  constraint uq_contacts_name   unique,
  email       text                      constraint  nn_contacts_email       not null  constraint uq_contacts_email  unique,
  phone       text                      constraint  nn_contacts_phone       not null,
  created_by  bigint                    constraint  nn_contacts_created_by  not null,
  created_at  timestamp with time zone  constraint  nn_contacts_created_at  not null  default current_timestamp,
  edited_by   bigint,
  edited_at   timestamp with time zone,
  
  constraint pk_contacts            primary key (id),
  constraint fk_contacts_created_by foreign key (created_by)  references principals(id),
  constraint fk_contacts_edited_by  foreign key (edited_by)   references principals(id)
);
comment on table contacts is 'Contact data.';

create table account_contact_types(
  id          bigint                    constraint  nn_account_contact_types_id         not null,
  name        text                      constraint  nn_account_contact_types_name       not null  constraint uq_account_contact_types_name   unique,
  created_by  bigint                    constraint  nn_account_contact_types_created_by not null,
  created_at  timestamp with time zone  constraint  nn_account_contact_types_created_at not null  default current_timestamp,
  edited_by   bigint,
  edited_at   timestamp with time zone,
  
  constraint pk_account_contact_types             primary key (id),
  constraint fk_account_contact_types_created_by  foreign key (created_by)  references principals(id),
  constraint fk_account_contact_types_edited_by   foreign key (edited_by)   references principals(id)
);
comment on table account_contact_types is 'Enumeration of contact types.';

create table account_contacts(
  account               bigint                    constraint  nn_account_contacts_account     not null,
  contact               bigint                    constraint  nn_account_contacts_contact     not null,
  account_contact_type  bigint,
  created_by            bigint                    constraint  nn_account_contacts_created_by  not null,
  created_at            timestamp with time zone  constraint  nn_account_contacts_created_at  not null  default current_timestamp,
  edited_by             bigint,
  edited_at             timestamp with time zone,
  
  constraint pk_account_contacts              primary key (account, contact),
  constraint fk_account_contacts_account      foreign key (account)               references accounts(id),
  constraint fk_account_contacts_contact      foreign key (contact)               references contacts(id),
  constraint fk_account_contacts_contact_type foreign key (account_contact_type)  references account_contact_types(id),
  constraint fk_account_contacts_created_by   foreign key (created_by)            references principals(id),
  constraint fk_account_contacts_edited_by    foreign key (edited_by)             references principals(id)
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
create sequence principals_seq            start with 1  increment by 1;
create sequence credidentials_seq         start with 1  increment by 1;
create sequence principal_groups_seq      start with 1  increment by 1;
create sequence principal_roles_seq       start with 1  increment by 1;
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

/*
 * 
 * DATA
 * 
 */
insert into principals(id, name, enabled, locked, created_by) values
(nextval('principals_seq'), 'system', true, false, currval('principals_seq'));

insert into principals(id, name, enabled, locked, created_by) values
(nextval('principals_seq'), 'root', true, false, currval('principals_seq'));

-- root:root
insert into credidentials(id, principal, value, created_by) values
(nextval('credidentials_seq'), (select id from principals where name = 'root'), '$2a$10$qLt58Ag/zJ9rOHfaP0AEPuE7pGZe7WY874CKMv/ZY5ZnzNqa1KH4C', (select id from principals where name = 'system'));

insert into operations(id, name) values 
(1, 'create'),
(2, 'read'),
(3, 'update'),
(4, 'delete');

insert into resources(id, name) values 
(1, 'accounts'),
(2, 'contacts'),
(3, 'addresses'),
(4, 'account_contacts'),
(5, 'account_addresses'),
(6, 'principal_groups'),
(7, 'principal_roles'),
(8, 'principal_group_memberships'),
(9, 'principal_role_memberships'),
(10, 'group_role_memberships'),
(11, 'role_permissions'),
(12, 'account_types'),
(13, 'account_contact_types'),
(14, 'account_address_types');

insert into principal_groups(id, name, created_by) values
(nextval('principal_groups_seq'), 'root', (select id from principals where name = 'system'));

insert into principal_roles(id, name, created_by) values
(nextval('principal_roles_seq'), 'root', (select id from principals where name = 'system'));

insert into group_role_memberships(principal_group, principal_role, created_by) values
((select id from principal_groups where name = 'root'), (select id from principal_roles where name = 'root'), (select id from principals where name = 'system'));

insert into principal_group_memberships(principal_group, principal, created_by) values
((select id from principal_groups where name = 'root'), (select id from principals where name = 'root'), (select id from principals where name = 'system'));

insert into principal_role_memberships(principal_role, principal, created_by) values
((select id from principal_roles where name = 'root'), (select id from principals where name = 'root'), (select id from principals where name = 'system'));

insert into role_permissions(principal_role, operation, resource, created_by)
select
  (select id from principal_roles where name = 'root'),
  operations.id,
  resources.id,
  (select id from principals where name = 'system')
from resources
cross join operations;

insert into account_types(id, name, created_by) values 
(1, 'Customer', (select id from principals where name = 'system')),
(2, 'Partner',  (select id from principals where name = 'system')),
(3, 'Investor', (select id from principals where name = 'system')),
(4, 'Reseller', (select id from principals where name = 'system'));

insert into account_address_types(id, name, created_by) values
(1, 'Billing',  (select id from principals where name = 'system')),
(2, 'Shipping', (select id from principals where name = 'system'));

insert into account_contact_types(id, name, created_by) values
(1, 'Seller',     (select id from principals where name = 'system')),
(2, 'Manager',    (select id from principals where name = 'system')),
(5, 'Developer',  (select id from principals where name = 'system')),
(6, 'Consultant', (select id from principals where name = 'system'));