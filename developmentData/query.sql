select * from m_user
select * from follows
select * from tweet

u.profile_image_url,
u.username,
u.name,
f.id_following

-- utk lihat siapa yg memfollow user 1 (follower)
select
u.profile_image_url,
u.username,
u.name,
f.id_follower
from m_user u
inner join follows f on u.id_user = f.id_follower 
where u.is_delete = false
and f.is_delete = false
and f.id_following = 1

-- utk lihat siapa yg di follow user 1 (following)
select
u.profile_image_url,
u.username,
u.name,
f.id_following
from m_user u
inner join follows f on u.id_user = f.id_following 
where u.is_delete = false
and f.is_delete = false
and f.id_follower = 1