BEGIN;
DROP TABLE IF EXISTS aggregation;
DROP TABLE IF EXISTS total_game_count;
DROP TABLE IF EXISTS game_sums;
CREATE TEMPORARY TABLE aggregation
SELECT COUNT(*)              AS games,
       c.uuid                as client_id,
       c.display_name        as display_name,
       sum(g.delta_winnings) as emeralds,
       conclusion
FROM game_result_entity g
         INNER JOIN
     transaction_entity t ON t.id = g.transaction_id
         INNER JOIN
     client_entity c ON c.uuid = t.client_id
GROUP BY g.conclusion, c.uuid, c.display_name
ORDER BY display_name;

CREATE TEMPORARY TABLE total_game_count
SELECT SUM(games)         as total_games,
       SUM(abs(emeralds)) as total_emeralds,
       client_id
FROM aggregation
GROUP BY client_id;

CREATE TEMPORARY TABLE game_sums
SELECT CAST(games / total_games as DECIMAL(15, 10))       as percentage,
       games,
       t.client_id,
       display_name,
       conclusion,
       CAST(emeralds / total_emeralds as DECIMAL(15, 10)) as emeralds,
       (SELECT CASE
                   WHEN conclusion = 'WIN' THEN 1
                   WHEN conclusion = 'LOSE' THEN -1
                   WHEN conclusion = 'BLACKJACK' THEN 1.5
                   ELSE 0
                   END)                                   as multiplier
FROM total_game_count t
         INNER JOIN
     aggregation a ON t.client_id = a.client_id;

SELECT *
FROM game_sums;

SELECT display_name,
       SUM(real_percentage) * 100 AS house_edge_by_game,
       SUM(emeralds) * - 100      AS house_edge_by_emeralds,
       client_id,
       SUM(games)                    game_count
FROM (SELECT *,
             - 1 * percentage * multiplier AS real_percentage
      FROM game_sums) AS g
GROUP BY client_id, display_name;


