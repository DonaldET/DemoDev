""" Extract token from a file and count frequency, build dependency tree """


def count_bounded_tokens(path, token_begin, token_end):
    """
    Extract the bounded token value from a record in the file and return token counts in a map.

    :param path: file with tokens
    :param token_begin: string marking beginning of token
    :param token_end: string marking end of token
    :return: map with tokens processed and counts.
    """
    collected = dict()
    collected['__MAIN__'] = str(path) + '|' + str(token_begin) + '|' + str(token_end)

    with open(path, 'r') as reader:
        line = reader.readline()
        while line:
            key = _extractor(line, token_begin, token_end)
            cnt = 0
            if key in collected:
                cnt = collected[key]
            cnt = cnt + 1
            collected[key] = cnt
            line = reader.readline()

    return collected


def find_bounded_tokens(path, token_begin, token_end):
    """
    Extract token value from a record in file record, building a list of files containing the token.

    :param path: file with tokens
    :param token_begin: string marking beginning of token
    :param token_end: string marking end of token
    :return: map with tokens processed and containing file lists.
    """
    collected = dict()
    collected['__MAIN__'] = [str(path) + '|' + str(token_begin) + '|' + str(token_end)]

    with open(path, 'r') as reader:
        line = reader.readline()
        while line:
            key = _extractor(line, token_begin, token_end)
            if not key.startswith('__'):
                refs = set()
                if key in collected:
                    refs = collected[key]
                refs.add(line[:line.find(':')])
                collected[key] = refs
            line = reader.readline()

    return collected


def _extractor(line, token_begin, token_end):
    """
    Extract and classify the token in a line.

    :param line: record of file with token
    :param token_begin: string marking beginning of token
    :param token_end: string marking end of token
    :return: the processed token, or its classification
    """
    if line is None:
        return 'None'

    line = str(line).strip()
    lth = len(line)
    if lth < 1:
        return '__empty'

    if lth < len(token_begin) + len(token_end) + 1:
        return '__short'

    tk_start = line.find(token_begin)
    if tk_start < 0:
        return '__missing'

    tk_end = line.find(token_end, tk_start + 1)
    if tk_end < 0:
        return '__error'

    if tk_start == tk_end:
        return '__zero'

    return line[tk_start + len(token_begin):tk_end]
