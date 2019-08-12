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
    sales double COMMENT 'sales total'<#if GEN_target == "PRODUCTION">,</#if>
<#assign seq_cols = ["units", "contracts"]>
<#assign seq_types = ["int", "int"]>
<#if GEN_target == "PRODUCTION">
	<#list seq_cols as q>
   	@@{q}  @@{seq_types[q_index]}<#if q_has_next>,</#if>
	</#list>
</#if>
  )
  COMMENT 'Sales Summary Report'
  STORED AS 'orc';
 
 INSERT OVERWRITE TABLE ${target_table}
 SELECT vehicle_bcode     as brand,
        sale_year         as year,
        sum(sales_amount) as sales
<#if GEN_target == "PRODUCTION">
	<#list seq_cols as q>
   		sum(@@{q})<#if q_has_next>,</#if>
	</#list>
</#if>
 FROM   ${source_table}
 WHERE vehicle_bcode in (${brand_list})
 GROUP BY vehicle_bcode, sale_year
 ORDER BY brand, year<#if GEN_target != "PRODUCTION"> LIMIT 10000</#if>;
