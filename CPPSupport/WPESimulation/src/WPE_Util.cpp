/*
 * WPE_Util.cpp
 *
 *  Created on: Sep 2, 2021
 *      Author: Donald Trummell
 */

#include "WPE_Util.hpp"

time_t get_current_time_ms() {
	struct timeval time_now { };
	gettimeofday(&time_now, nullptr);
	time_t msecs_time = ((long) (time_now.tv_sec) * 1000L)
			+ ((long) (time_now.tv_usec) / 1000L);
	return (msecs_time);
}
