##
# A sample test application configuration with Replacement
##
app_title=@@{title}
an_object_id=@@{exampleObject.name}
an_object_value=@@{exampleObject.value}
# List deployable systems
<#list systems as system>
sys_num.@@{system_index + 1}=@@{system.name} from @@{system.value}
</#list>
a_configuration=<%= @environment_prefix %>@@{exampleObject.name}.@@{exampleObject.value}
b_configuration=${sombody_else}