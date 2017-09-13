select
  ad.address_code,
  ad.street,
  ad.city,
  ad.postal_code,
  ad.state,
  ad.country
from t_addresses ad
where ad.address_id = :address_id
