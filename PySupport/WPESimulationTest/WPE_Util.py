#
#  WPE_Util.cpp
# 
#   Provide real time
#
from datetime import datetime


def get_current_time_ms() -> int:
    dt = datetime.now()
    return dt.second * 10000 + (dt.microsecond + 500) // 1000
