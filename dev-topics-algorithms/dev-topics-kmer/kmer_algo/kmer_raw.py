import time

"""
kmer_raw.py

Compute all KMER combinations using construction of list for each new KMER. This algorithm is taken from a Medium
article at https://towardsdatascience.com/how-fast-is-c-compared-to-python-978f18f474c7, and written by Naser Tamimi
(see https://tamimi-naser.medium.com/.)

Minor edits were made to meet PEP-8 requirements, along with some timing code.
"""

def convert(c):
    if c == 'A':
        return 'C'
    if c == 'C':
        return 'G'
    if c == 'G':
        return 'T'
    if c == 'T':
        return 'A'


print("Start raw article algorithm")

opt = "ACGT"
s = ""
s_last = ""
len_str = 13

for i in range(len_str):
    s += opt[0]

for i in range(len_str):
    s_last += opt[-1]

pos = 0
counter = 1
start_time_ns = time.monotonic_ns()  # Added timing code
while s != s_last:
    # You can uncomment the next line to see all k-mers.
    # print(f"{counter}\t{s}")
    counter += 1
    change_next = True
    for i in range(len_str):
        if change_next:
            if s[i] == opt[-1]:
                s = s[:i] + convert(s[i]) + s[i + 1:]
                change_next = True
            else:
                s = s[:i] + convert(s[i]) + s[i + 1:]
                break
# You can uncomment the next line to see all k-mers.
# print(f"{counter}\t{s}")
end_time_ns = time.monotonic_ns()  # Added timing code
elapsed = float(end_time_ns - start_time_ns) / 1000000000.0

print(f"Number of generated k-mers: {counter}, elapsed time: {elapsed} seconds.")
print("Finish!")
