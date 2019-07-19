-- hive_vars.hql
--
-- Defines Hive variables used by the Hive query. An overview of Hive variables is found at
-- https://cwiki.apache.org/confluence/display/Hive/LanguageManual+VariableSubstitution.
--
--Generation date: Run date: Jul 18, 2019 5:38:21 PM

set tbl_prefix=PRODUCTION;
set base_source_table_name=VehicleSales;
set source_table=${tbl_prefix}_${base_source_table_name};
set base_target_table_name=SalesSummary;
set target_table=${tbl_prefix}_${base_source_table_name};

set prod_brands="CHEVY", "FORD", "TOYOTA", "LEXUS", "HONDA", "BMW", "VW";
set nonprod_brands="CHEVY", "TOYOTA";
set brand_list=${prod_brands};
