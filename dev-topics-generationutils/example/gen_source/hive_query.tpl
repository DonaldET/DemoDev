-- hive_query.hql
--
-- Query source table creating a summary depending on environment
--
-- Note: query is<#if GEN_target != "PRODUCTION"> not</#if> optimized for <#if GEN_target == "PRODUCTION">PRODUCTION<#else>Non-production</#if>
--
-- Define parameters as variables for run
source hive_vars.hql;

use ${db_name};

DROP TABLE ${target_table} IF EXISTS;

CREATE TABLE IF NOT EXISTS ${target_table}
  (
   brand string COMMENT 'brand code',
   year  double COMMENT 'sales year',
   sales double COMMENT 'sales total'
  )
  COMMENT 'Sales Summary'
  STORED AS 'orc';
 
 INSERT OVERWRITE TABLE ${target_table}
 SELECT vechicle-bcode    as brand,
        sale_year         as year,
        sum(sales_amount) as sales
 FROM   ${source_table}
 WHERE bcode in (${brand_list})
 GROUP BY bcode, sale_year
 ORDER BY brand, year
 <#if GEN_target != "PRODUCTION">LIMIT 10000</#if>;
