select count(g.conclusion), c.username, t.client_id
from game_result_entity g
         left join transaction_entity t on g.transaction_id = t.id
         left join client_entity c on t.client_id = c.uuid
where g.name = 'BLACKJACK'
group by c.id, c.display_name, c.username, c.name, t.client_id
order by count(g.id) desc;


select row_number() over (order by c.date_created) row_id,
       t.date_created,
       conclusion,
       original_bet,
       delta_winnings,
       t.client_id
from game_result_entity g
         left join transaction_entity t on g.transaction_id = t.id
         left join client_entity c on t.client_id = c.uuid
where g.name = 'BLACKJACK'

