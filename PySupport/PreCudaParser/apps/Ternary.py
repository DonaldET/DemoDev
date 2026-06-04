# Python Test conditional expression NO side effects
#
# initial:: chooser: True  x: 1  y: 5  z: 0
# post::    chooser: True  x: 1  y: 5  z: 2
#
# initial:: chooser: False  x: 1  y: 5  z: 2
# post::    chooser: False  x: 1  y: 5  z: 6ser:  False  x: 1  y: 1  z: 1

def main():
    chooser: bool = True
    x: int = 1
    y: int = 5
    z: int = 0

    print("\nPython Test conditional expression NO side-effects")
    print(f"\ninitial:: chooser: {chooser}  x: {x}  y: {y}  z: {z}")
    z = x + 1 if chooser else y + 1
    print(f"post::    chooser: {chooser}  x: {x}  y: {y}  z: {z}")
    chooser = False
    print(f"\ninitial:: chooser: {chooser}  x: {x}  y: {y}  z: {z}")
    z = x + 1 if chooser else y + 1
    print(f"post::    chooser: {chooser}  x: {x}  y: {y}  z: {z}")
    print()
    return 0


if __name__ == '__main__':
    main()
