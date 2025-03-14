create database rtc_chat_monitor on cluster logs;

create table rtc_chat_monitor.rtc_message on cluster logs (
    channel_name VARCHAR(255),
    account_name VARCHAR(255),
    message text,
    msgId VARCHAR(255),
    timestamp DATETIME64
) engine = Null;


create materialized view rtc_chat_monitor.rtc_message_count_by_hour_mv on cluster logs
    to rtc_message_count_by_hour
    as select channel_name,
              toDate(timestamp) as date,
              toHour(timestamp) as hour,
              count(msgId)
           from rtc_message
group by (channel_name, date, hour)
;

create table rtc_chat_monitor.rtc_message_count_by_hour on cluster logs (
    channel_name VARCHAR(255),
    date Date32,
    hour UInt8,
    total AggregateFunction(sum, Nullable(UInt64))
) engine = AggregatingMergeTree() ORDER BY (channel_name, date, hour);

