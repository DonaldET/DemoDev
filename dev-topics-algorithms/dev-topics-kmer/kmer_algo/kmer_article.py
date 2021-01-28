import time

"""
kmer_article.py

Compute all kmer combinations using construction of list

"""

# D:\Temp2\kmer\kmer_algo>python kmer_article.py
# Start KMER Computation by Constructing Lists
# Nucleotides: ACGT;  Sequence Length: 13;  K-MERs Generated: 67108864
# First: AAAAAAAAAAAAA
# Last : TTTTTTTTTTTTT
# Number of generated k-mers: 67108864
# Elapsed time: 41.078 secs
# Finished!

nucleotides = "ACGT"
nucleotides_rotation = {'A': 'C', 'C': 'G', 'G': 'T', 'T': 'A'}


def _build_by_append(s, pos, new_nucleotide):
    last = len(s) - 1
    if pos > 0:
        if pos < last:
            s_new = s[0:pos] + new_nucleotide + s[pos + 1:]
        else:
            s_new = s[0:last] + new_nucleotide
    else:
        s_new = new_nucleotide + s[1:]
    return s_new


def process(len_str):
    first_base = nucleotides[0]
    s = ""
    for _ in range(len_str):
        s += first_base
    print(f"First: {s}")

    s_last = ""
    for _ in range(len_str):
        s_last += nucleotides[-1]
    print(f"Last : {s_last}")

    count = 1
    # if display:
    # print(f"{count}.\t{s}")
    start_time_ns = time.monotonic_ns()
    while s != s_last:
        count += 1

        pos = len_str - 1
        while pos >= 0:
            nc = nucleotides_rotation[s[pos]]
            s = _build_by_append(s, pos, nc)
            if nc != first_base:
                break
            pos -= 1

        # if display:
        # print(f"{count}.\t{s}")
    end_time_ns = time.monotonic_ns()
    return count, float(end_time_ns - start_time_ns) / 1000000.0


if __name__ == '__main__':
    print("Start KMER Computation by Constructing Lists")
    nucleotides_len = 2
    total_sequences = round(float(len(nucleotides)) ** nucleotides_len)
    print(
        f"Nucleotides: {str(nucleotides)};  Sequence Length: {str(nucleotides_len)};  \
K-MERs expected: {total_sequences:.0f}")
    kmer_count, elapsed_mu = process(nucleotides_len)
    print("Number of generated k-mers: {}".format(kmer_count))
    if kmer_count != total_sequences:
        print(f"--- WARNING: possible error, expected {total_sequences} KMERs but got {kmer_count}")
    print(f"Elapsed time: {elapsed_mu / 1000.0} secs")
    print("Finished!")
