select
  c.contact_code,
  c.person_name,
  c.email,
  c.phone
from t_contacts c
where c.contact_id = :contact_id
