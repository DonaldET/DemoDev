//Simplest C++ Transformation Test File
//initial:: chooser: true  x: 2  y: 6  z: -777
//post::    chooser: true  x: 2  y: 6  z: 2
//initial:: chooser: false  x: 2  y: 6  z: 2
//post::    chooser: false  x: 2  y: 6  z: 6:    chooser: false  x: 2  y: 6  z: 7

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

int main()
{
    printf("\nSimplest C++ Transformation Test File");
    int x = 2, y = 6, z = -777;

    bool chooser = true;
    printf("\ninitial:: chooser: %s  x: %d  y: %d  z: %d", chooser ? "true" : "false", x, y, z);
    z = chooser ? x : y;
    printf("\npost::    chooser: %s  x: %d  y: %d  z: %d", chooser ? "true" : "false", x, y, z);
    chooser = false;
    printf("\ninitial:: chooser: %s  x: %d  y: %d  z: %d", chooser ? "true" : "false", x, y, z);
    z = chooser ? x : y;
    printf("\npost::    chooser: %s  x: %d  y: %d  z: %d", chooser ? "true" : "false", x, y, z);
    return 0;
}
