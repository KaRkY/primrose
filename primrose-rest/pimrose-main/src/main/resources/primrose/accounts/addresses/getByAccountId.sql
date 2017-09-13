select
  ad.address_code,
  ad.street,
  ad.city,
  ad.postal_code,
  ad.state,
  ad.country,
  aat.account_address_type_code
from t_addresses ad
left join t_account_addresses aad on aad.address_id = ad.address_id
left join t_account_address_types aat on aat.account_address_type_id = aad.account_address_type_id
where aad.account_id = :account_id
