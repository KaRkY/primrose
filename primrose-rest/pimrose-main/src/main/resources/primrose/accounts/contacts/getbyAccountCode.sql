select
  c.contact_code,
  c.person_name,
  c.email,
  c.phone,
  act.account_contact_type_code
from t_contacts c
left join t_account_contacts ac on ac.contact_id = c.contact_id
left join t_accounts a on a.account_id = ac.account_id
left join t_account_contact_types act on act.account_contact_type_id = ac.account_contact_type_id
where a.account_code = :account_code