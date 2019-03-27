#include "ccvector.h"
#include "common.h"

int VecCreate(CC_VECTOR **Vector)
{
    if (Vector == NULL)
    {
        return -1;
    }
    *Vector = (CC_VECTOR*)malloc(sizeof(CC_VECTOR));
    if (*Vector == NULL)
    {
        return -1;
    }
    (*Vector)->Size = 0;
    (*Vector)->Capacity = 27;
    (*Vector)->Memory = (int*)malloc((*Vector)->Capacity * sizeof(int));
    if ((*Vector)->Memory == NULL)
    {
        free(*Vector);
        *Vector = NULL;
        return -1;
    }

    return 0;
}

int VecDestroy(CC_VECTOR **Vector)
{
    if (Vector == NULL) {
        return -1;
    }

    if (*Vector == NULL)
    {
        return -1;
    }

    if ((*Vector)->Memory != NULL)
    {
        free((*Vector)->Memory);
        (*Vector)->Memory = NULL;
    }
    (*Vector)->Capacity = 0;
    (*Vector)->Size = 0;
    free(*Vector);
    *Vector = NULL;

    return 0;
}

int VecInsertTail(CC_VECTOR *Vector, int Value)
{
    if (Vector == NULL)
    {
        return -1;
    }
    if (Vector->Capacity == Vector->Size)
    {
        Vector->Capacity = Vector->Capacity * 2;
        Vector->Memory = (int*)realloc(Vector->Memory, Vector->Capacity * sizeof(int));
        if (Vector->Memory == NULL)
        {
            return -1;
        }
    }
    Vector->Memory[Vector->Size] = Value;
    (Vector->Size)++;

    return 0;
}

int VecRotate(CC_VECTOR *Vector, int StartPosition, int Shift)
{
    if (Vector == NULL)
    {
        return -1;
    }

    if (Shift > Vector->Size - 1)
    {
        return -1;
    }

    if (StartPosition > Vector->Size - 1)
    {
        return -1;
    }

    while (Shift > 0)
    {
        int temp = Vector->Memory[Vector->Size - 1];
        for (int i = (Vector->Size) - 1; i > StartPosition; i--)
        {
            Vector->Memory[i] = Vector->Memory[i - 1];
        }
        Vector->Memory[StartPosition] = temp;
        Shift--;
    }

    return 0;
}

int VecInsertHead(CC_VECTOR *Vector, int Value)
{
    if (Vector == NULL)
    {
        return -1;
    }
    if (Vector->Capacity == Vector->Size)
    {
        Vector->Capacity = Vector->Capacity * 2;
        Vector->Memory = (int*)realloc(Vector->Memory, Vector->Capacity * sizeof(int));
        if (Vector->Memory == NULL)
        {
            return -1;
        }
    }
    Vector->Memory[Vector->Size] = Value;
    (Vector->Size)++;
    VecRotate(Vector, 0, 1);

    return 0;
}

int VecInsertAfterIndex(CC_VECTOR *Vector, int Index, int Value)
{
    if (Vector == NULL)
    {
        return -1;
    }

    if ((Index > Vector->Size - 1) ||Index < 0)
    {
        return -1;
    }

    if (Vector->Capacity == Vector->Size)
    {
        Vector->Capacity = Vector->Capacity * 2;
        Vector->Memory = (int*)realloc(Vector->Memory, Vector->Capacity * sizeof(int));
        if (Vector->Memory == NULL)
        {
            return -1;
        }
    }
    Vector->Memory[Vector->Size] = Value;
    (Vector->Size)++;
    VecRotate(Vector, Index + 1, 1);

    return 0;
}

int VecRemoveByIndex(CC_VECTOR *Vector, int Index)
{
    if (Vector == NULL)
    {
        return -1;
    }

    if (Index > Vector->Size - 1|| Index < 0)
    {
        return -1;
    }
    Vector->Memory[Index] = 0;
    VecRotate(Vector, Index, (Vector->Size) - 1 - Index);
    Vector->Size--;

    return 0;
}

int VecGetValueByIndex(CC_VECTOR *Vector, int Index, int *Value)
{
    if (Vector == NULL)
    {
        return -1;
    }

    if (Index > Vector->Size - 1||Index < 0)
    {
        return -1;
    }
    
    *Value = Vector->Memory[Index];

	return 0;
}

int VecGetCount(CC_VECTOR *Vector)
{
	if (Vector == NULL)
	{
		return -1;
	}
	return Vector->Size;
}

int VecClear(CC_VECTOR *Vector)
{
    if (Vector == NULL)
    {
        return -1;
    }
    VecDestroy(&Vector);
    VecCreate(&Vector);
	return 0;
}

int VecSort(CC_VECTOR *Vector)
{
    if (Vector == NULL)
    {
        return -1;
    }
    int aux=0;
    for (int i = 0; i < Vector->Size; i++)
    {
        for (int j = 0; j < (Vector->Size) - i - 1; j++)
        {
            if (Vector->Memory[j] > Vector->Memory[j + 1])
            {
                aux = Vector->Memory[j];
                Vector->Memory[j] = Vector->Memory[j + 1];
                Vector->Memory[j + 1] = aux;
            }
        }
    }
	return 0;
}
