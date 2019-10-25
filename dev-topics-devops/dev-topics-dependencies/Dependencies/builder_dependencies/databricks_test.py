import ntpath
import sys
import myapppy.generate as gen
from myapppy import who_am_i

def _main_wai():
    who_am_i(sys.stdout, ntpath.basename("Databricks\Cell"))


def _main_gen():
    f = gen.fib()
    for i in range(6):
        print(str(i) + " . . . " + str(next(f)))

print("---------++++----------")
print("Test Who Am I?")
_main_wai()

print("\n---------++++----------")
print("Test Fibber Generator")
_main_gen()
