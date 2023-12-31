package com.easy.sdk.common.extra.mybatis.constant;

import java.util.Set;

import cn.hutool.core.collection.CollUtil;

/**
 * 关键字枚举
 * @author 薛超
 * @since 2019年8月1日
 * @version 1.0.5
 */
public interface KeyWordConstant {

	/**
	 * 是否包含关键字
	 * @param keyWord 目前只有mysql关键字
	 * @return boolean 是否包含
	 */
	static boolean contains(String keyWord) {
		return MYSQL_KEY_WORD_5_7.contains(keyWord.toLowerCase());
	}

	/**
	 * 622个关键字
	 */
	Set<String> MYSQL_KEY_WORD_5_7 = CollUtil.newHashSet("accessible", "account", "action", "add", "after", "against",
			"aggregate", "algorithm", "all", "alter", "always", "analyse", "analyze", "and", "any", "as", "asc",
			"ascii", "asensitive", "at", "autoextend_size", "auto_increment", "avg", "avg_row_length", "backup",
			"before", "begin", "between", "bigint", "binary", "binlog", "bit", "blob", "block", "bool", "boolean",
			"both", "btree", "by", "byte", "cache", "call", "cascade", "cascaded", "case", "catalog_name", "chain",
			"change", "changed", "channel", "char", "character", "charset", "check", "checksum", "cipher",
			"class_origin", "client", "close", "coalesce", "code", "collate", "collation", "column", "columns",
			"column_format", "column_name", "comment", "commit", "committed", "compact", "completion", "compressed",
			"compression", "concurrent", "condition", "connection", "consistent", "constraint", "constraint_catalog",
			"constraint_name", "constraint_schema", "contains", "context", "continue", "convert", "cpu", "create",
			"cross", "cube", "current", "current_date", "current_time", "current_timestamp", "current_user", "cursor",
			"cursor_name", "data", "database", "databases", "datafile", "date", "datetime", "day", "day_hour",
			"day_microsecond", "day_minute", "day_second", "deallocate", "dec", "decimal", "declare", "default",
			"default_auth", "definer", "delayed", "delay_key_write", "delete", "desc", "describe", "des_key_file",
			"deterministic", "diagnostics", "directory", "disable", "discard", "disk", "distinct", "distinctrow", "div",
			"do", "double", "drop", "dual", "dumpfile", "duplicate", "dynamic", "each", "else", "elseif", "enable",
			"enclosed", "encryption", "end", "ends", "engine", "engines", "enum", "error", "errors", "escape",
			"escaped", "event", "events", "every", "exchange", "execute", "exists", "exit", "expansion", "expire",
			"explain", "export", "extended", "extent_size", "false", "fast", "faults", "fetch", "fields", "file",
			"file_block_size", "filter", "first", "fixed", "float", "float4", "float8", "flush", "follows", "for",
			"force", "foreign", "format", "found", "from", "full", "fulltext", "function", "general", "generated",
			"geometry", "geometrycollection", "get", "get_format", "global", "grant", "grants", "group",
			"group_replication", "handler", "hash", "having", "help", "high_priority", "host", "hosts", "hour",
			"hour_microsecond", "hour_minute", "hour_second", "identified", "if", "ignore", "ignore_server_ids",
			"import", "in", "index", "indexes", "infile", "initial_size", "inner", "inout", "insensitive", "insert",
			"insert_method", "install", "instance", "int", "int1", "int2", "int3", "int4", "int8", "integer",
			"interval", "into", "invoker", "io", "io_after_gtids", "io_before_gtids", "io_thread", "ipc", "is",
			"isolation", "issuer", "iterate", "join", "json", "key", "keys", "key_block_size", "kill", "language",
			"last", "leading", "leave", "leaves", "left", "less", "level", "like", "limit", "linear", "lines",
			"linestring", "list", "load", "local", "localtime", "localtimestamp", "lock", "locks", "logfile", "logs",
			"long", "longblob", "longtext", "loop", "low_priority", "master", "master_auto_position", "master_bind",
			"master_connect_retry", "master_delay", "master_heartbeat_period", "master_host", "master_log_file",
			"master_log_pos", "master_password", "master_port", "master_retry_count", "master_server_id", "master_ssl",
			"master_ssl_ca", "master_ssl_capath", "master_ssl_cert", "master_ssl_cipher", "master_ssl_crl",
			"master_ssl_crlpath", "master_ssl_key", "master_ssl_verify_server_cert", "master_tls_version",
			"master_user", "match", "maxvalue", "max_connections_per_hour", "max_queries_per_hour", "max_rows",
			"max_size", "max_statement_time", "max_updates_per_hour", "max_user_connections", "medium", "mediumblob",
			"mediumint", "mediumtext", "memory", "merge", "message_text", "microsecond", "middleint", "migrate",
			"minute", "minute_microsecond", "minute_second", "min_rows", "mod", "mode", "modifies", "modify", "month",
			"multilinestring", "multipoint", "multipolygon", "mutex", "mysql_errno", "name", "names", "national",
			"natural", "nchar", "ndb", "ndbcluster", "never", "new", "next", "no", "nodegroup", "nonblocking", "none",
			"not", "no_wait", "no_write_to_binlog", "null", "number", "numeric", "nvarchar", "offset", "old_password",
			"on", "one", "only", "open", "optimize", "optimizer_costs", "option", "optionally", "options", "or",
			"order", "out", "outer", "outfile", "owner", "pack_keys", "page", "parser", "parse_gcol_expr", "partial",
			"partition", "partitioning", "partitions", "password", "phase", "plugin", "plugins", "plugin_dir", "point",
			"polygon", "port", "precedes", "precision", "prepare", "preserve", "prev", "primary", "privileges",
			"procedure", "processlist", "profile", "profiles", "proxy", "purge", "quarter", "query", "quick", "range",
			"read", "reads", "read_only", "read_write", "real", "rebuild", "recover", "redofile", "redo_buffer_size",
			"redundant", "references", "regexp", "relay", "relaylog", "relay_log_file", "relay_log_pos", "relay_thread",
			"release", "reload", "remove", "rename", "reorganize", "repair", "repeat", "repeatable", "replace",
			"replicate_do_db", "replicate_do_table", "replicate_ignore_db", "replicate_ignore_table",
			"replicate_rewrite_db", "replicate_wild_do_table", "replicate_wild_ignore_table", "replication", "require",
			"reset", "resignal", "restore", "restrict", "resume", "return", "returned_sqlstate", "returns", "reverse",
			"revoke", "right", "rlike", "rollback", "rollup", "rotate", "routine", "row", "rows", "row_count",
			"row_format", "rtree", "savepoint", "schedule", "schema", "schemas", "schema_name", "second",
			"second_microsecond", "security", "select", "sensitive", "separator", "serial", "serializable", "server",
			"session", "set", "share", "show", "shutdown", "signal", "signed", "simple", "slave", "slow", "smallint",
			"snapshot", "socket", "some", "soname", "sounds", "source", "spatial", "specific", "sql", "sqlexception",
			"sqlstate", "sqlwarning", "sql_after_gtids", "sql_after_mts_gaps", "sql_before_gtids", "sql_big_result",
			"sql_buffer_result", "sql_cache", "sql_calc_found_rows", "sql_no_cache", "sql_small_result", "sql_thread",
			"sql_tsi_day", "sql_tsi_hour", "sql_tsi_minute", "sql_tsi_month", "sql_tsi_quarter", "sql_tsi_second",
			"sql_tsi_week", "sql_tsi_year", "ssl", "stacked", "start", "starting", "starts", "stats_auto_recalc",
			"stats_persistent", "stats_sample_pages", "status", "stop", "storage", "stored", "straight_join", "string",
			"subclass_origin", "subject", "subpartition", "subpartitions", "super", "suspend", "swaps", "switches",
			"table", "tables", "tablespace", "table_checksum", "table_name", "temporary", "temptable", "terminated",
			"text", "than", "then", "time", "timestamp", "timestampadd", "timestampdiff", "tinyblob", "tinyint",
			"tinytext", "to", "trailing", "transaction", "trigger", "triggers", "true", "truncate", "type", "types",
			"uncommitted", "undefined", "undo", "undofile", "undo_buffer_size", "unicode", "uninstall", "union",
			"unique", "unknown", "unlock", "unsigned", "until", "update", "upgrade", "usage", "use", "user",
			"user_resources", "use_frm", "using", "utc_date", "utc_time", "utc_timestamp", "validation", "value",
			"values", "varbinary", "varchar", "varcharacter", "variables", "varying", "view", "virtual", "wait",
			"warnings", "week", "weight_string", "when", "where", "while", "with", "without", "work", "wrapper",
			"write", "x509", "xa", "xid", "xml", "xor", "year", "year_month", "zerofill");

}
