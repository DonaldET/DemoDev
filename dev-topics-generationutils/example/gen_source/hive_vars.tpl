; hive_vars.hql
;
; Defines Hive variables used by the Hive query. A good overview is found at
; https://cwiki.apache.org/confluence/display/Hive/LanguageManual+VariableSubstitution.
;
set evn_prefix=@@{GEN_env_prefix};
set base_source_table_name=@@{GEN_src_tbl};
set source_table=${}_${base_source_table_name};
