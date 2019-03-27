#include "ccstack.h"
#include "common.h"

int StCreate(CC_STACK **Stack)
{
    if (Stack==NULL)
    {
        return -1;
    }

    *Stack = (CC_STACK*)malloc(sizeof(CC_STACK));
    if (*Stack == NULL)
    {
        return -1;
    }

    int retValue = VecCreate(&(*Stack)->Vector);
    if (retValue == -1)
    {
        return -1;
    }

    (*Stack)->Top = 0;
    return 0;
}

int StDestroy(CC_STACK **Stack)
{
    if (Stack == NULL)
    {
        return -1;
    }

    if (*Stack == NULL)
    {
        return -1;
    }

    int retValue = VecDestroy(&(*Stack)->Vector);
    if (retValue == -1)
    {
        return -1;
    }
    (*Stack)->Top = 0;
    free(*Stack);
    *Stack = NULL;

    return 0;
}

int StPush(CC_STACK *Stack, int Value)
{
    if (Stack == NULL)
    {
        return -1;
    }
    VecInsertTail(Stack->Vector, Value);
    (Stack->Top)++;
    return 0;
}

int StPop(CC_STACK *Stack, int *Value)
{
    if (Stack == NULL)
    {
        return -1;
    }
    *Value = (Stack->Vector)->Memory[(Stack->Top) - 1];
    VecRemoveByIndex(Stack->Vector, (Stack->Top) - 1);
    (Stack->Top) --;
    return 0;
}

int StPeek(CC_STACK *Stack, int *Value)
{
    if (Stack == NULL)
    {
        return -1;
    }
    *Value = (Stack->Vector)->Memory[(Stack->Top)-1];
  
    return 0;
}

int StIsEmpty(CC_STACK *Stack) 
{
    if (Stack == NULL)
    {
        return -1;
    }

    if (Stack->Top == 0)
    {
        return 1;
    }
    return 0;
}

int StGetCount(CC_STACK *Stack)
{
    if (Stack == NULL)
    {
        return -1;
    }
    
    return Stack->Top;
}

int StClear(CC_STACK *Stack)
{
    if (Stack == NULL)
    {
        return -1;
    }
    StDestroy(&Stack);
    StCreate(&Stack);
    return 0;
}

int StPushStack(CC_STACK *Stack, CC_STACK *StackToPush)
{ 
    if (Stack == NULL || StackToPush == NULL)
    {
        return -1;
    }
    int sizeStackToPushForFree = StackToPush->Top;
    int *values=(int*)malloc(sizeStackToPushForFree *sizeof(int));

    if (values == NULL)
    {
        return -1;
    }

    int contor=0;
    while (0 != StIsEmpty(StackToPush))
    {
        StPop(StackToPush, &values[contor]);
        contor++;
    }
    
    while (0 != contor)
    {
        StPush(Stack,values[contor-1]);
        contor--;
    }
    free(values);
    return 0;
}
