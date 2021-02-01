import time

"""
kmer_article_c.py

Compute all kmer combinations like an odometer using C-like representation strategy
"""

nucleotides = "ACGT"
nc = len(nucleotides)
nucleotides_rotation = (1, 2, 3, 0)


def process(len_str):
    first_base = 0
    s = len_str * [first_base]
    print(f"First: {list(map(lambda x: nucleotides[x], s))}")

    last_base = nc - 1
    s_last = len_str * [last_base]
    print(f"Last : {list(map(lambda x: nucleotides[x], s_last))}")

    count = 1
    # if display:
    #     print(f"{count}.\t{s}")
    start_time_ns = time.monotonic_ns()
    while s != s_last:
        count += 1

        pos = len_str - 1
        while pos >= 0:
            s[pos] = nucleotides_rotation[s[pos]]
            if s[pos] != first_base:
                break
            pos -= 1

            # if display:
            #     print(f"{count}.\t{s}")
    end_time_ns = time.monotonic_ns()
    return count, float(end_time_ns - start_time_ns) / 1000000.0


if __name__ == '__main__':
    print("Start KMER Computation using C approach")
    nucleotides_len = 13
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
