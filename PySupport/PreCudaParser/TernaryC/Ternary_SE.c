//Test ternary IF
// TRUE
//initial:: chooser:  true  x: 0  y: 0
//post::    chooser:  true  x: 1  y: 1
// FALSE
//initial:: chooser:  false  x: 1  y: 1
//post::    chooser:  false  x: 1  y: 2
//
//Incremented x: 4
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

int x = 0, y = 0;
bool chooser = true;

int main()
{
    printf("\nC++ Test ternary IF operator with side-effects");
    chooser = true;
    printf("\ninitial:: chooser: %s x: %d y: %d", chooser ? "true" : "false", x, y);
    y = chooser ? ++x : ++y;
    printf("\npost::    chooser: %s x: %d y: %d", chooser ? "true" : "false", x, y);
    chooser = true;
    printf("\ninitial:: chooser: %s x: %d y: %d", chooser ? "true" : "false", x, y);
    y = chooser ? ++x : ++y;
    printf("\npost::    chooser: %s x: %d y: %d", chooser ? "true" : "false", x, y);
    ++x;
    ++x;
    ++x;
    printf("\nIncremented x: %d", x);
}
