select toString(hour) as hours, sum(total) as total
from (select hour as hour, countMerge(total) as total
 from rtc_chat_monitor.global_rtc_message_count_by_hour
 group by hour

 union all

 select number as hour, 0 as total

 from numbers(0, 24))
group by hour
order by hour;