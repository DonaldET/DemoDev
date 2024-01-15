//============================================================================
// Name        : Streamer.cpp
// Author      : Don Trummell
// Version     :
// Copyright   : Copyright (c) 2023 - Don Trummell
// Description : C++ Version of Java streaming example
//============================================================================

#include <chrono>
#include <cmath>
#include <iostream>

using namespace std;

typedef std::chrono::high_resolution_clock Clock;

const int SIZE = 1223;
const int SPECIAL = 93;
const int SPECIAL_VALUE = 997;

int data[SIZE];

void setup_data() {
	for (int i = 0; i < SIZE; i++) {
		data[i] = i;
	}
}

int simulate(int term) {
	return (term / SPECIAL) == 0 ?
			SPECIAL_VALUE :
			term + (int) std::lround(sin((double) term) * (double) 25.0);
}

class Processor {

public:
	virtual int evaluate(int observations[], int lth) = 0;
};

class CPPSimulate: public Processor {
public:
	int evaluate(int observations[], int lth) override {
		int result = 0;
		for (int i = 0; i < lth; i++) {
			result += simulate(observations[i]);
		}
		return result;
	}
};

void runTest(string label, int observations[], int lth) {
	cout << "Timing for " << label;

	CPPSimulate processor;
	auto start_time = Clock::now();
	int result = processor.evaluate(observations, lth);
	auto end_time = Clock::now();
	long elapsed_nano = std::chrono::duration_cast<std::chrono::nanoseconds>(
			end_time - start_tim).count();
	cout << "Result: " << result << "; elapsed: " << elapsed_nano << " nanosecs"
			<< endl;
}

int main() {
	setup_data();
	cout << "Testing List of " << SIZE << " observations in C++" << endl;
	runTest("CPP Normal\t", data, SIZE);
	return 0;
}
