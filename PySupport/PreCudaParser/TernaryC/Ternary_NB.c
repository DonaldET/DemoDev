//C++ Test NO ternary operator NO side-effects
//Setup:   x: 1  y: 5  Alternatives: [6, 2]
//post::   chooser: true -> 1  z: 2
//post::   chooser: false -> 0  z: 6

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

int main()
{
    printf("\nC++ Test NO ternary operator NO side-effects");
    int x = 1, y = 5, z = 0;

    int alternatives[2];
    alternatives[0] = y + 1;
    alternatives[1] = x + 1;
    printf("\nSetup:   x: %d  y: %d  Alternatives: [%d, %d]", x, y, alternatives[0], alternatives[1]);
    bool chooser = true;
    int idx = !!chooser;
    z = alternatives[idx];
    printf("\npost::   chooser: %s -> %d  z: %d", chooser ? "true" : "false", idx, z);
    chooser = false;
    idx = !!chooser;
    z = alternatives[idx];
    printf("\npost::   chooser: %s -> %d  z: %d", chooser ? "true" : "false", idx, z);
    return 0;
}
