select
  a.account_code,
  act.account_type_code,
  pa.account_code as parent_account_code,
  a.display_name,
  a.full_name,
  a.email,
  a.phone,
  a.website,
  a.description,
  a.valid_from,
  a.valid_to
from t_accounts a
left join t_accounts pa on pa.account_id = a.parent_account_id
left join t_account_types act on act.account_type_id = a.account_type_id
where a.account_code = :code
