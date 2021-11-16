#
#  WPE_Util.cpp
# 
#   Provide wall-clock actual time
#
from datetime import datetime


def get_current_time_ms() -> int:
    dt = datetime.now()
    return dt.day * 86400 * 1000 + (dt.hour * 60 + dt.minute) * 60 * 1000 + dt.second * 1000 + (
            dt.microsecond + 500) // 1000
