//C++ Test ternary IF operator NO side-effects
//initial:: chooser: true  x: 0  y: 0  z: 0
//post::    chooser: true  x: 0  y: 0  z: 1
//initial:: chooser: false  x: 0  y: 0  z: 1
//post::    chooser: false  x: 0  y: 0  z: 1

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

int main()
{
    int x = 0, y = 0, z = 0;

    printf("\nC++ Test ternary IF operator NO side-effects");
    bool chooser = true;
    printf("\ninitial:: chooser: %s  x: %d  y: %d  z: %d", chooser ? "true" : "false", x, y, z);
    z = chooser ? x + 1 : y + 1;
    printf("\npost::    chooser: %s  x: %d  y: %d  z: %d", chooser ? "true" : "false", x, y, z);
    chooser = false;
    printf("\ninitial:: chooser: %s  x: %d  y: %d  z: %d", chooser ? "true" : "false", x, y, z);
    z = chooser ? x + 1 : y + 1;
    printf("\npost::    chooser: %s  x: %d  y: %d  z: %d", chooser ? "true" : "false", x, y, z);
    return 0;
}
