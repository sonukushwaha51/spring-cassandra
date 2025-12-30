CREATE KEYSPACE IF NOT EXISTS trigger_notification
WITH replication = {
  'class': 'SimpleStrategy',
  'replication_factor': 1
};

USE trigger_notification;

CREATE TABLE IF NOT EXISTS trigger_notification.notification (
  user_id text,
  event_id text,
  notification_id text,
  timestamp bigint,
  items text,
  PRIMARY KEY ((user_id), event_id)
);