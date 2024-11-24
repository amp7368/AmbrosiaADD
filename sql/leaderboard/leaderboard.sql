select (select c.display_name
        from client_entity c
        where t.client_id = c.id)        cName,
       sum(g.delta_winnings) / 4096 / 64 winnings,
       t.client_id,
       count(*)                          games_count
from game_result_entity g
         left join transaction_entity t on g.transaction_id = t.id
group by t.client_id
order by winnings desc;

select winnings, cName
from (select (select c.display_name
              from client_entity c
              where t.client_id = c.id)   cName,
             avg(g.delta_winnings / 4096) winnings,
             t.client_id,
             count(*)                     games_count
      from game_result_entity g
               left join transaction_entity t on g.transaction_id = t.id
      group by t.client_id
      order by winnings desc) q
where games_count > 100;
