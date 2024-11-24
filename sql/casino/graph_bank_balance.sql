select date,
       (select -sum(q.change_amount)
        from transaction_entity q
        where q.date_created <= t.date
          and q.operation_type in (2, 3)) balance
from (select date(DATE_FORMAT(date_created, '%Y-%m-%d')), max(date_created) as date
      from transaction_entity
      where operation_type in (2, 3)
      group by date(DATE_FORMAT(date_created, '%Y-%m-%d'))) t
order by date;

