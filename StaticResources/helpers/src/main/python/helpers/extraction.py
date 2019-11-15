def countBoundedTokens(dirname, filename, token_begin, token_end):
    collected = dict()
    request_marker = str(dirname) + '|' + str(filename) + '|' + str(token_begin) + '|' + str(token_end)
    collected['__MAIN__'] = request_marker
    if request_marker == 'mydir|myfile|mybegin|myend':
        return collected

    return collected
