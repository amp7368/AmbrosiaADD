select *
from transaction_entity
         left join game_result_entity on transaction_entity.id = game_result_entity.transaction_id
where conclusion = 'SPLIT'
order by date_created desc;

select (select count(g.delta_winnings)
        from game_result_entity g
                 left join transaction_entity t on g.transaction_id = t.id
        where delta_winnings > 0
          and t.client_id = 280)    wins,
       (select count(g.delta_winnings)
        from game_result_entity g
                 left join transaction_entity t on g.transaction_id = t.id
        where delta_winnings < 0
          and t.client_id = 280) as losses,
       (select count(g.delta_winnings)
        from game_result_entity g
                 left join transaction_entity t on g.transaction_id = t.id
        where delta_winnings = 0
          and t.client_id = 280) as ties;


