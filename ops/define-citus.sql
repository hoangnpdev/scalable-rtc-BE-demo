
SELECT citus_set_coordinator_host('citus1', 5432)
SELECT * from citus_add_node('citus2', 5432)
SELECT * from citus_add_node('citus3', 5432)
SELECT * FROM citus_get_active_worker_nodes();
