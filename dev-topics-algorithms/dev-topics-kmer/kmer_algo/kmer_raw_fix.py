import time

"""
kmer_raw_fix.py

Compute all KMER combinations using construction of list for each new KMER. This algorithm is taken from a Medium
article at https://towardsdatascience.com/how-fast-is-c-compared-to-python-978f18f474c7, and written by Naser Tamimi
(see https://tamimi-naser.medium.com/.)

Minor edits were made to meet PEP-8 requirements, along with some timing code. A timing run was also pasted into
the code as a comment below. The inner loop was simplified as well.
"""


# D:\GitHub\DemoDev\dev-topics-algorithms\dev-topics-kmer\kmer_algo>python kmer_raw_fix.py
# Start fixed raw article algorithm
# Number of generated k-mers: 67108864, elapsed time: 70.063 seconds.
# Finish!


def convert(c):
    if c == 'A':
        return 'C'
    if c == 'C':
        return 'G'
    if c == 'G':
        return 'T'
    if c == 'T':
        return 'A'


print("Start fixed raw article algorithm")

opt = "ACGT"
ender = opt[-1]
s = ""
s_last = ""
len_str = 13

for i in range(len_str):
    s += opt[0]

for i in range(len_str):
    s_last += ender

pos = 0
counter = 1
start_time_ns = time.monotonic_ns()  # Added timing code
while s != s_last:
    # You can uncomment the next line to see all k-mers.
    # print(f"{counter}\t{s}")
    counter += 1
    for i in range(len_str):
        done = s[i] != ender
        s = s[:i] + convert(s[i]) + s[i + 1:]
        if done:
            break
# You can uncomment the next line to see all k-mers.
# print(f"{counter}\t{s}")
end_time_ns = time.monotonic_ns()  # Added timing code
elapsed = float(end_time_ns - start_time_ns) / 1000000000.0

print(f"Number of generated k-mers: {counter}, elapsed time: {elapsed} seconds.")
print("Finish!")
