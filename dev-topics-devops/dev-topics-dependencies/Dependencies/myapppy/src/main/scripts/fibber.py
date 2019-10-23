import myapppy.generate as gen


def _main():
    f = gen.fib()
    for i in range(6):
        print(str(i) + " . . . " + str(next(f)))


if __name__ == '__main__':
    _main()
