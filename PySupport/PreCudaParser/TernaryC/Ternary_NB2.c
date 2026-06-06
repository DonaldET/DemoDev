//C++ Test NO nested ternary operator NO side-effects
//Setup:   w: 1  x: 2  y: 3  Alternatives: [3, 2, 1, 1]
//          : transforming >>> z = chooser1 ? w : (chooser2 ? x : y)
//post::   chooser1: true   chooser2: false ->    idx[2]  z: 1
//post::   chooser1: true   chooser2: true ->     idx[3]  z: 1
//post::   chooser1: false  chooser2: false ->    idx[0]  z: 3
//post::   chooser1: false  chooser2: true ->     idx[1]  z: 2

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

int main()
{
    printf("\nC++ Test NO nested ternary operator NO side-effects");
    int w = 1, x = 2, y = 3, z = -777;
    // Note: transforming z = chooser1 ? w : (chooser2 ? x : y)
    int alternatives[4];
    alternatives[0] = y;
    alternatives[1] = x;
    alternatives[2] = w;
    alternatives[3] = w;
    printf("\nSetup:   w: %d  x: %d  y: %d  Alternatives: [%d, %d, %d, %d]", w, x, y, alternatives[0], alternatives[1], alternatives[2], alternatives[3]);
    printf("\n          : transforming >>> z = chooser1 ? w : (chooser2 ? x : y)");
    bool chooser1 = true;
    bool chooser2 = false;
    int idx = 2 * !!chooser1 + !!chooser2;
    z = alternatives[idx];
    printf("\npost::   chooser1: %s   chooser2: %s ->\tidx[%d]  z: %d", chooser1 ? "true" : "false", chooser2 ? "true" : "false", idx, z);
    chooser2 = true;
    idx = 2 * !!chooser1 + !!chooser2;
    z = alternatives[idx];
    printf("\npost::   chooser1: %s   chooser2: %s ->\tidx[%d]  z: %d", chooser1 ? "true" : "false", chooser2 ? "true" : "false", idx, z);
    chooser1 = false;
    chooser2 = false;
    idx = 2 * !!chooser1 + !!chooser2;
    z = alternatives[idx];
    printf("\npost::   chooser1: %s  chooser2: %s ->\tidx[%d]  z: %d", chooser1 ? "true" : "false", chooser2 ? "true" : "false", idx, z);
    chooser2 = true;
    idx = 2 * !!chooser1 + !!chooser2;
    z = alternatives[idx];
    printf("\npost::   chooser1: %s  chooser2: %s ->\tidx[%d]  z: %d", chooser1 ? "true" : "false", chooser2 ? "true" : "false", idx, z);
    return 0;
}
