create database rtc_chat_monitor on cluster logs;

create table rtc_chat_monitor.rtc_message on cluster logs (
    channel_name VARCHAR(255),
    account_name VARCHAR(255),
    message text,
    msgId VARCHAR(255),
    timestamp DATETIME64
) engine = MergeTree
order by (channel_name, account_name, msgId);

drop table rtc_chat_monitor.rtc_message on cluster logs;

create table rtc_chat_monitor.global_rtc_message on cluster logs
    as rtc_chat_monitor.rtc_message
 engine = Distributed(logs, rtc_chat_monitor, rtc_message, xxHash32(channel_name));

drop table rtc_chat_monitor.global_rtc_message on cluster logs;

create materialized view rtc_chat_monitor.rtc_message_count_by_hour_mv on cluster logs
    to rtc_chat_monitor.rtc_message_count_by_hour
    as select channel_name,
              toDate(timestamp) as date,
              toHour(timestamp) as hour,
              countState(msgId) as total
           from rtc_chat_monitor.rtc_message
group by (channel_name, date, hour);

drop table rtc_chat_monitor.rtc_message_count_by_hour_mv on cluster logs;

create table rtc_chat_monitor.rtc_message_count_by_hour on cluster logs (
    channel_name VARCHAR(255),
    date Date32,
    hour UInt8,
    total AggregateFunction(count, UInt64)
) engine = AggregatingMergeTree() ORDER BY (channel_name, date, hour);

drop table rtc_chat_monitor.rtc_message_count_by_hour on cluster logs;

create table rtc_chat_monitor.global_rtc_message_count_by_hour on cluster logs
    as rtc_chat_monitor.rtc_message_count_by_hour
    engine = Distributed('logs', rtc_chat_monitor, rtc_message_count_by_hour);

drop table rtc_chat_monitor.global_rtc_message_count_by_hour on cluster logs;


select channel_name, date, hour, countMerge(total)
from rtc_chat_monitor.global_rtc_message_count_by_hour
group by channel_name, date, hour;

select channel_name, date, hour, count(total)
from remote('clickhouse03', rtc_chat_monitor.rtc_message_count_by_hour, 'admin', '123456')
group by channel_name, date, hour;

insert into rtc_chat_monitor.global_rtc_message
(*) VALUES ('test', 'test', 'test', '1', now64());

select *
from rtc_chat_monitor.global_rtc_message;



