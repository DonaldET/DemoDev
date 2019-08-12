<#-- -->
<#-- MyGenWrapper.tpl -->
<#-- -->
<#-- Final Compose Model -->
<#-- cp: {an_object_value=Balboa Park Station,
<#--      override_me=overridden_again, -->
<#--      app_title=My Glorious Application, -->
<#--      an_object_id=1250 Plymouth, -->
<#--      override_specific=by_override, -->
<#--      override_more_specific=by_override2} -->
<#-- -->
#Before import
<#import "/MyGenLibrary.tpl" as lb>
#after import
# Before include
<#-- Add a header for a specific model/context -->
<#if override_more_specific == "by_override2">
<#include "/MyGenOverride2Header.tpl">
</#if>
# After include
location=${@@{an_object_value}}
launch_point=${location}/@@{an_object_id}
source=<@lb.src_path header="ssss" leaf=an_object_value/>
destination=<@lb.dst_path header="dddd" leaf=an_object_id/>
