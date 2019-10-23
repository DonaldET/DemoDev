""" Mathematical generation Utilities """


def fib():
    """
    Fibonacci Numbers, one per invocation
    :return: next fibonacci integer
    """
    a, b = 0, 1
    while True:
        yield b
        a, b = b, a + b
