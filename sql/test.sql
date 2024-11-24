select *
from client_entity c
where username like '%Threads%';
select *
from client_entity;

select sum(t.change_amount)
from transaction_entity t
         left join game_result_entity g on g.transaction_id = t.id
where t.client_id = 275;


SELECT COUNT(*) / (SELECT count(*)
                   from transaction_entity t
                            left join game_result_entity g on t.id = g.transaction_id
                   where t.client_id = c.uuid
                     and g.id is not null) * 100 AS percent,
       COUNT(*)                                  AS games,
       c.username,
       conclusion
FROM game_result_entity g
         left JOIN
     transaction_entity t ON t.id = g.transaction_id
         left JOIN
     client_entity c ON c.uuid = t.client_id
where t.client_id = 275
GROUP BY g.conclusion, t.client_id, c.uuid, c.display_name;


SELECT COUNT(*) / (SELECT count(*)
                   from transaction_entity t
                            left join game_result_entity g on t.id = g.transaction_id
                   where t.client_id = c.uuid
                     and g.id is not null) * 100 AS percent,
       COUNT(*)                                  AS games,
       c.username,
       conclusion
FROM game_result_entity g
         left JOIN
     transaction_entity t ON t.id = g.transaction_id
         left JOIN
     client_entity c ON c.uuid = t.client_id
GROUP BY g.conclusion, t.client_id, c.display_name

ORDER BY percent desc;


SELECT concat('"', table_schema, '"."', table_name, '"') AS table_name
FROM information_schema.tables;


select sum(change_amount) / 4096 / 64 amount
from transaction_entity
where operation_type in (0, 1, 6, 2, 3)

select sum(change_amount) / 4096 / 64 amount
from transaction_entity
where operation_type in (2, 3,6)

