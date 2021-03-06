-- hive_query.hql
--
-- Query source table creating a summary depending on environment
--
-- Note: query is optimized for PRODUCTION
--
-- Define parameters as variables for run
source hive_vars.hql;
use ${db_name};

DROP TABLE ${target_table} IF EXISTS;

CREATE TABLE IF NOT EXISTS ${target_table}
  (
    brand string COMMENT 'brand code',
    year  double COMMENT 'sales year',
    sales double COMMENT 'sales total',
   	units  int,
   	contracts  int
  )
  COMMENT 'Sales Summary Report'
  STORED AS 'orc';
 
 INSERT OVERWRITE TABLE ${target_table}
 SELECT vehicle_bcode     as brand,
        sale_year         as year,
        sum(sales_amount) as sales
   		sum(units),
   		sum(contracts)
 FROM   ${source_table}
 WHERE vehicle_bcode in (${brand_list})
 GROUP BY vehicle_bcode, sale_year
 ORDER BY brand, year;
