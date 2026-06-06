//C++ Test Transformation of Ternary IF operator NO side-effects
//initial:: chooser: true  x: 2  y: 6  z: 0
//post::    chooser: true  x: 2  y: 6  z: 2
//initial:: chooser: false  x: 2  y: 6  z: 2
//post::    chooser: false  x: 2  y: 6  z: 6
//
//Setup:   w: 1  x: 2  y: 6  z: -777
//          : transforming >>> z = chooser1 ? w : (chooser2 ? x : y)
//post::   chooser1: true   chooser2: false -> z: 1
//post::   chooser1: true   chooser2: true -> z: 1
//post::   chooser1: false   chooser2: false -> z: 6
//post::   chooser1: false   chooser2: true -> z: 2

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

int main()
{
    printf("\nC++ Test Transformation of Ternary IF operator NO side-effects");
    int x = 2, y = 6, z = 0;

    bool chooser = true;
    printf("\ninitial:: chooser: %s  x: %d  y: %d  z: %d", chooser ? "true" : "false", x, y, z);
    z = chooser ? x : y;
    printf("\npost::    chooser: %s  x: %d  y: %d  z: %d", chooser ? "true" : "false", x, y, z);
    chooser = false;
    printf("\ninitial:: chooser: %s  x: %d  y: %d  z: %d", chooser ? "true" : "false", x, y, z);
    z = chooser ? x : y;
    printf("\npost::    chooser: %s  x: %d  y: %d  z: %d", chooser ? "true" : "false", x, y, z);

    int w = 1;
    x = 2;
    y = 6;
    z = -777;
    // Note: transforming z = chooser1 ? w : (chooser2 ? x : y)
    printf("\n\nSetup:   w: %d  x: %d  y: %d  z: %d", w, x, y, z);
    printf("\n          : transforming >>> z = chooser1 ? w : (chooser2 ? x : y)");
    bool chooser1 = true;
    bool chooser2 = false;
    z = chooser1 ? w : (chooser2 ? x : y);
    printf("\npost::   chooser1: %s   chooser2: %s -> z: %d", chooser1 ? "true" : "false", chooser2 ? "true" : "false", z);
    chooser2 = true;
    z = chooser1 ? w : (chooser2 ? x : y);
    printf("\npost::   chooser1: %s   chooser2: %s -> z: %d", chooser1 ? "true" : "false", chooser2 ? "true" : "false", z);
    chooser1 = false;
    chooser2 = false;
    z = chooser1 ? w : (chooser2 ? x : y);
    printf("\npost::   chooser1: %s   chooser2: %s -> z: %d", chooser1 ? "true" : "false", chooser2 ? "true" : "false", z);
    chooser2 = true;
    z = chooser1 ? w : (chooser2 ? x : y);
    printf("\npost::   chooser1: %s   chooser2: %s -> z: %d", chooser1 ? "true" : "false", chooser2 ? "true" : "false", z);
    return 0;
}
