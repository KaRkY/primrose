with recursive groups(group_id, group_code, group_name, group_parent) as (

  select
    g.group_id,
    g.group_code,
    g.group_name,
    g.group_parent
  from t_groups g
  inner join t_user_groups ug on ug.user_group_group_id = g.group_id
  where ug.user_group_user_id = :user_id
  and   group_parent is null

  union all

  select
    g.group_id,
    g.group_code,
    g.group_name,
    g.group_parent
  from t_groups g
  inner join groups gg on gg.group_id = g.group_parent
)

select distinct
  p.permission_code,
  p.permission_name
from groups g
inner join t_group_permissions gp on gp.group_permission_group_id = g.group_id
inner join t_permissions p on p.permission_id = gp.group_permission_permisson_id
