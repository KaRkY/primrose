with recursive gr(id, name) as (
  select principal_groups.id, principal_groups.name
  from principal_groups
  join principal_group_memberships on principal_group_memberships.principal_group = principal_groups.id
  join principals on principals.id = principal_group_memberships.principal
  where principals.name = 'root'

  union all

  select principal_groups.id, principal_groups.name
  from principal_groups
  join gr on gr.id = principal_groups.parent
)
select resources.name || ':' || operations.name as granted_authority
from gr
join group_role_memberships on group_role_memberships.principal_group = gr.id
join principal_roles on principal_roles.id = group_role_memberships.principal_role
join role_permissions on role_permissions.principal_role = principal_roles.id
join operations on operations.id = role_permissions.operation
join resources on resources.id = role_permissions.resource

union

select resources.name || ':' || operations.name as granted_authority
from principals
join principal_role_memberships on principal_role_memberships.principal = principals.id
join principal_roles on principal_roles.id = principal_role_memberships.principal_role
join role_permissions on role_permissions.principal_role = principal_roles.id
join operations on operations.id = role_permissions.operation
join resources on resources.id = role_permissions.resource
where principals.name = 'root';


