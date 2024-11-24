select *
from roulette_table_game;

select *
from roulette_player_game;

select *
from roulette_player_bet;


select count(*)
from game_result_entity g
         left join transaction_entity t on g.transaction_id = t.id
where t.client_id = 290;

select *
from transaction_entity
order by date_created;