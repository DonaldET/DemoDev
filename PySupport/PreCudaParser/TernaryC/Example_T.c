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
    char *alternatives_0[2];
    alternatives_0[0] = "false";
    alternatives_0[1] = "true";
    int idx_0 = !!chooser;
    printf("\ninitial:: chooser: %s  x: %d  y: %d  z: %d", alternatives_0[idx_0], x, y, z);
    int alternatives_1[2];
    alternatives_1[0] = y;
    alternatives_1[1] = x;
    int idx_1 = !!chooser;
    z = alternatives_1[idx_1];
    char *alternatives_2[2];
    alternatives_2[0] = "false";
    alternatives_2[1] = "true";
    int idx_2 = !!chooser;
    printf("\npost::    chooser: %s  x: %d  y: %d  z: %d", alternatives_2[idx_2], x, y, z);
    chooser = false;
    char *alternatives_3[2];
    alternatives_3[0] = "false";
    alternatives_3[1] = "true";
    int idx_3 = !!chooser;
    printf("\ninitial:: chooser: %s  x: %d  y: %d  z: %d", alternatives_3[idx_3], x, y, z);
    int alternatives_4[2];
    alternatives_4[0] = y;
    alternatives_4[1] = x;
    int idx_4 = !!chooser;
    z = alternatives_4[idx_4];
    char *alternatives_5[2];
    alternatives_5[0] = "false";
    alternatives_5[1] = "true";
    int idx_5 = !!chooser;
    printf("\npost::    chooser: %s  x: %d  y: %d  z: %d", alternatives_5[idx_5], x, y, z);

    int w = 1;
    x = 2;
    y = 6;
    z = -777;
    // Note: transforming z = chooser1 ? w : (chooser2 ? x : y)
    printf("\n\nSetup:   w: %d  x: %d  y: %d  z: %d", w, x, y, z);
    printf("\n          : transforming >>> z = chooser1 ? w : (chooser2 ? x : y)");
    bool chooser1 = true;
    bool chooser2 = false;
    int alternatives_6[4];
    alternatives_6[0] = y;
    alternatives_6[1] = x;
    alternatives_6[2] = w;
    alternatives_6[3] = w;
    int idx_6 = 2 * !!chooser1 + !!chooser2;
    z = alternatives_6[idx_6];
    char *alternatives_7[2];
    alternatives_7[0] = "false";
    alternatives_7[1] = "true";
    int idx_7 = !!chooser1;
    char *alternatives_8[2];
    alternatives_8[0] = "false";
    alternatives_8[1] = "true";
    int idx_8 = !!chooser2;
    printf("\npost::   chooser1: %s   chooser2: %s -> z: %d", alternatives_7[idx_7], alternatives_8[idx_8], z);
    chooser2 = true;
    int alternatives_9[4];
    alternatives_9[0] = y;
    alternatives_9[1] = x;
    alternatives_9[2] = w;
    alternatives_9[3] = w;
    int idx_9 = 2 * !!chooser1 + !!chooser2;
    z = alternatives_9[idx_9];
    char *alternatives_10[2];
    alternatives_10[0] = "false";
    alternatives_10[1] = "true";
    int idx_10 = !!chooser1;
    char *alternatives_11[2];
    alternatives_11[0] = "false";
    alternatives_11[1] = "true";
    int idx_11 = !!chooser2;
    printf("\npost::   chooser1: %s   chooser2: %s -> z: %d", alternatives_10[idx_10], alternatives_11[idx_11], z);
    chooser1 = false;
    chooser2 = false;
    int alternatives_12[4];
    alternatives_12[0] = y;
    alternatives_12[1] = x;
    alternatives_12[2] = w;
    alternatives_12[3] = w;
    int idx_12 = 2 * !!chooser1 + !!chooser2;
    z = alternatives_12[idx_12];
    char *alternatives_13[2];
    alternatives_13[0] = "false";
    alternatives_13[1] = "true";
    int idx_13 = !!chooser1;
    char *alternatives_14[2];
    alternatives_14[0] = "false";
    alternatives_14[1] = "true";
    int idx_14 = !!chooser2;
    printf("\npost::   chooser1: %s   chooser2: %s -> z: %d", alternatives_13[idx_13], alternatives_14[idx_14], z);
    chooser2 = true;
    int alternatives_15[4];
    alternatives_15[0] = y;
    alternatives_15[1] = x;
    alternatives_15[2] = w;
    alternatives_15[3] = w;
    int idx_15 = 2 * !!chooser1 + !!chooser2;
    z = alternatives_15[idx_15];
    char *alternatives_16[2];
    alternatives_16[0] = "false";
    alternatives_16[1] = "true";
    int idx_16 = !!chooser1;
    char *alternatives_17[2];
    alternatives_17[0] = "false";
    alternatives_17[1] = "true";
    int idx_17 = !!chooser2;
    printf("\npost::   chooser1: %s   chooser2: %s -> z: %d", alternatives_16[idx_16], alternatives_17[idx_17], z);
    return 0;
}
