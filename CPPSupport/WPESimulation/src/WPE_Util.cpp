/*
 * WPE_Util.cpp
 *
 *  Created on: Sep 2, 2021
 *      Author: Donald Trummell
 */

#include "WPE_Util.hpp"
#include <sys/time.h>

long get_current_time_ms() {
	struct timeval time_now { };
	gettimeofday(&time_now, nullptr);
	return (time_now.tv_sec * 1000L) + ((time_now.tv_usec + 500) / 1000L);
}
