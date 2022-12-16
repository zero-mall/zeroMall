insert into product
(cat_id, naver_id, brand, product_name, image_url, price, avg_star, view_count, like_count, stand_price, modified_at, registered_at)
values
('001001001')
on duplicate key update cat_id = values(cat_id)