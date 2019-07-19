<#assign rd_token=3 * GEN_tasks?number + 3>
{"name": "read_token", "tasks": @@{GEN_tasks}, "memory": "@@{GEN_memory}", "value": @@{rd_token}}