# Java Test NO ternary operator NO side effects
# Setup:   x: 1  y: 5  Alternatives: [2, 6]
# post::   chooser: 0  z: 2
# post::   chooser: 1  z: 6

def main():
    x: int = 1
    y: int = 5
    z: int = 0

    print("\nJava Test NO ternary operator NO side-effects")
    alternatives: list[int] = [x + 1, y + 1]
    print(f"Setup:   x: {x}  y: {y}  z: {z}  Alternatives: [{alternatives[0]}, {alternatives[1]}]")
    chooser: int = 0
    z = alternatives[chooser]
    print(f"post::   chooser: {chooser}  z: {z}")
    chooser = 1
    z = alternatives[chooser]
    print(f"post::   chooser: {chooser}  z: {z}")


if __name__ == '__main__':
    main()
