; hive_query.hql
;
; Query source table creating a summary depending on environment
;
; Note: query is<#if GEN_target != "PRODUCTION"> not</#if> optimized
;
source hive_vars.hql;

use ${db_name};

CREATE TABLE IF NOT EXISTS ${table_name}
  (
   brand string COMMENT 'brand code',
   year double COMMENT 'sales year'
   sales double COMMENT 'sales total'
  )
  COMMENT 'Sales Summary']
  STORED AS 'orc';
 
 