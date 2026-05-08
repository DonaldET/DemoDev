#
# Test ternary IF
# initial:: chooser:  True  x: 0  y: 0
# post::    chooser:  True  x: 1  y: 1
#
# initial:: chooser:  False  x: 1  y: 1
# post::    chooser:  False  x: 2  y: 2
#
# Incremented x: 5
#

def main():
    print("\nTest ternary IF")
    x: int = 0
    y: int = 0
    chooser: bool = True
    print(f"initial:: chooser:  {chooser}  x: {x}  y: {y}")
    x += 1
    y += 1
    y = x if chooser else y
    print(f"post::    chooser:  {chooser}  x: {x}  y: {y}")
    chooser = False
    print(f"\ninitial:: chooser:  {chooser}  x: {x}  y: {y}")
    x += 1
    y += 1
    y = x if chooser else y
    print(f"post::    chooser:  {chooser}  x: {x}  y: {y}")
    x += 1
    x += 1
    x += 1
    print(f"\nIncremented x: {x}")


if __name__ == '__main__':
    main()
