select
  u.user_code,
  u.user_name,
  u.user_enabled,
  u.user_locked,
  uu.user_username,
  up.user_password
from t_users u
left join t_user_usernames uu on uu.user_user_id = u.user_id and current_timestamp between uu.valid_from and uu.valid_to
left join t_user_passwords up on up.user_user_id = u.user_id and current_timestamp between up.valid_from and up.valid_to
where u.user_id = :user_id