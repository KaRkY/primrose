insert into t_contacts(
  contact_id,
  person_name,
  email,
  phone,
  address_id
) values (
  :contact_id,
  :person_name,
  :email,
  :phone,
  (select address_id from t_addresses where address_code = :address_code)
)

