//C++ Test NO ternary operator NO side-effects
//Setup:   x: 1  y: 5  Alternatives: [2, 6]
//post::   chooser: 0  z: 2
//post::   chooser: 1  z: 6

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

int main()
{
    int x = 1, y = 5, z = 0;

    printf("\nC++ Test NO ternary operator NO side-effects");
    int *alternatives = malloc(2 * sizeof(int));
    alternatives[0] = x + 1;
    alternatives[1] = y + 1;
    printf("\nSetup:   x: %d  y: %d  Alternatives: [%d, %d]", x, y, alternatives[0], alternatives[1]);
    int chooser = 0;
    z = alternatives[chooser];
    printf("\npost::   chooser: %d  z: %d", chooser, z);
    chooser = 1;
    z = alternatives[chooser];
    printf("\npost::   chooser: %d  z: %d", chooser, z);
    return 0;
}
