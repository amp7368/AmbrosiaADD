select t.date_created, g.*
from game_result_entity g
         left join transaction_entity t on g.transaction_id = t.id
where g.name = 'BLACKJACK'
order by id;

select *
from game_result_entity;



SET @player := 5;
SET @total_games := (SELECT count(*)
                     from game_result_entity g
                              inner join transaction_entity t on t.id = g.transaction_id
                     where t.client_id = @player);

SELECT COUNT(*) / @total_games * 100 AS percent,
       COUNT(*)                      AS games,
       c.display_name,
       conclusion
FROM game_result_entity g
         INNER JOIN
     transaction_entity t ON t.id = g.transaction_id
         INNER JOIN
     client_entity c ON c.uuid = t.client_id
GROUP BY g.conclusion, t.client_id, display_name
ORDER BY display_name;

