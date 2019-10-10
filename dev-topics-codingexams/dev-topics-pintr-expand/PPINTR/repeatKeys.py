import collections

"""
Write a function that expands a HashMap to an array. The HashMap contains a key and a value, from which your function
will produce an array with the key repeated the respective number of times. Output order of the array does not matter.
 
Convert a dictionary with key:count pairs to an array where each array entry has the key "value" replicated count times.
"""


def _build_frequencies(text):
    freq = dict()
    if text is None:
        return freq
    txt = str(text).strip(" ")
    if len(txt) < 1:
        return freq
    words = text.split()
    if len(words) < 1:
        return freq
    return collections.Counter(words)


def _repeat_key(occurrence):
    repeats = []
    for key in occurrence:
        repeats.append(str(key) * occurrence[key])
    return repeats


def run_test(label, input_str):
    print("\n" + label + ":\n  - " + str(input_str))
    freq_by_key = _build_frequencies(input_str)
    print("  - Frequencies: " + str(freq_by_key))
    keys = _repeat_key(freq_by_key)
    print("  - Keys       : " + str(keys))


if __name__ == '__main__':
    run_test("None", None)
    run_test("Empty", "")
    run_test("One word", "Hello")
    run_test("Unique words", "Hello There!")
    doi = "We really really need to be really really faster than our " \
          "competition who need to be slower than us to win"
    run_test("Some Repeats", doi)
