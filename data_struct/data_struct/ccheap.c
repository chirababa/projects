#include "ccheap.h"
#include "common.h"
#include <string.h>

void ReconstituieMaxheap(CC_HEAP *MaxHeap, int Parent)
{
    int maxim = 0, aux = 0;
    int leftChild = 2 * Parent + 1;
    int rightChild = 2 * Parent + 2;
    if (leftChild < MaxHeap->BodyHeap->Size && MaxHeap->BodyHeap->Memory[leftChild] > MaxHeap->BodyHeap->Memory[Parent])
    {
        maxim = leftChild;
    }
    else
    {
        maxim = Parent;
    }
    if (rightChild < MaxHeap->BodyHeap->Size && MaxHeap->BodyHeap->Memory[rightChild] > MaxHeap->BodyHeap->Memory[maxim])
    {
        maxim = rightChild;
    }
    if (maxim != Parent)
    {
        aux = MaxHeap->BodyHeap->Memory[Parent];
        MaxHeap->BodyHeap->Memory[Parent] = MaxHeap->BodyHeap->Memory[maxim];
        MaxHeap->BodyHeap->Memory[maxim] = aux;
        ReconstituieMaxheap(MaxHeap,maxim);
    }
}

void ReconstituieMinheap(CC_HEAP *MinHeap, int Parent)
{
    int minim = 0, aux = 0;
    int leftChild = 2 * Parent + 1;
    int rightChild = 2 * Parent + 2;
    if (leftChild < MinHeap->BodyHeap->Size && MinHeap->BodyHeap->Memory[leftChild] < MinHeap->BodyHeap->Memory[Parent])
    {
        minim = leftChild;
    }
    else
    {
        minim = Parent;
    }
    if (rightChild <MinHeap->BodyHeap->Size && MinHeap->BodyHeap->Memory[rightChild] < MinHeap->BodyHeap->Memory[minim])
    {
        minim = rightChild;
    }
    if (minim != Parent)
    {
        aux = MinHeap->BodyHeap->Memory[Parent];
        MinHeap->BodyHeap->Memory[Parent] = MinHeap->BodyHeap->Memory[minim];
        MinHeap->BodyHeap->Memory[minim] = aux;
        ReconstituieMinheap(MinHeap, minim);
    }
}

void bottomup(CC_HEAP *Heap)
{
    if (Heap->HeapType == 0)
    {
        for (int i = ((Heap->BodyHeap->Size) - 1) / 2; i >= 0; i--)
        {
            ReconstituieMinheap(Heap, i);
        }
    }
    else
    {
        for (int i = ((Heap->BodyHeap->Size) - 1) / 2; i >= 0; i--)
        {
            ReconstituieMaxheap(Heap, i);
        }
    }
}

int HpCreateMaxHeap(CC_HEAP **MaxHeap, CC_VECTOR* InitialElements)
{
    if (MaxHeap == NULL)
    {
        return -1;
    }

    *MaxHeap = (CC_HEAP*)malloc(sizeof(CC_HEAP));

    if (*MaxHeap == NULL)
    {
        return -1;
    }

    int retValue = VecCreate(&(*MaxHeap)->BodyHeap);
    if (retValue == -1)
    {
        return -1;
    }
    
    (*MaxHeap)->HeapType = 1;

    if (InitialElements != NULL)
    {
        if (InitialElements->Size > (*MaxHeap)->BodyHeap->Size) {
            (*MaxHeap)->BodyHeap->Memory = (int*)realloc((*MaxHeap)->BodyHeap->Memory, ((InitialElements->Size) - (*MaxHeap)->BodyHeap->Size) * sizeof(int));
            if ((*MaxHeap)->BodyHeap->Memory == NULL)
            {
                return -1;
            }
            (*MaxHeap)->BodyHeap->Size = InitialElements->Size;
        }
        memcpy((*MaxHeap)->BodyHeap->Memory, InitialElements->Memory,(sizeof(InitialElements->Memory)));
        bottomup(*MaxHeap);
    }

    return 0;
}

int HpCreateMinHeap(CC_HEAP **MinHeap, CC_VECTOR* InitialElements)
{
    if (MinHeap == NULL)
    {
        return -1;
    }

    *MinHeap = (CC_HEAP*)malloc(sizeof(CC_HEAP));

    if (*MinHeap == NULL)
    {
        return -1;
    }

    int retValue = VecCreate(&(*MinHeap)->BodyHeap);
    if (retValue == -1)
    {
        return -1;
    }

    (*MinHeap)->HeapType = 0;

    if (InitialElements!= NULL)
    {
        if (InitialElements->Size > (*MinHeap)->BodyHeap->Size)
        {
            (*MinHeap)->BodyHeap->Memory = (int*)realloc((*MinHeap)->BodyHeap->Memory, ((InitialElements->Size) - (*MinHeap)->BodyHeap->Size) * sizeof(int));
            if ((*MinHeap)->BodyHeap->Memory == NULL)
            {
                return -1;
            }
            (*MinHeap)->BodyHeap->Size = InitialElements->Size;
        }
        memcpy((*MinHeap)->BodyHeap->Memory, InitialElements->Memory, (sizeof(InitialElements->Memory)));
        bottomup(*MinHeap);
    }
    return 0;
}

int HpDestroy(CC_HEAP **Heap)
{
    if (Heap == NULL)
    {
        return -1;
    }

    if (*Heap == NULL)
    {
        return -1;
    }
    int retValue = VecDestroy(&((*Heap)->BodyHeap));
    if (retValue == -1)
    {
        return -1;
    }
    free(*Heap);
    *Heap = NULL;
    return 0;
}

int HpInsert(CC_HEAP *Heap, int Value)
{
    if (Heap == NULL)
    {
        return -1;
    }

    int retValue = VecInsertTail(Heap->BodyHeap,Value);
    if (retValue == -1)
    {
        return -1;
    }
    bottomup(Heap);
    return 0;
}

int HpRemove(CC_HEAP *Heap, int Value)
{
    if (Heap == NULL)
    {
        return -1;
    }
    int valueForReturn=-1;
    for (int i = 0; i < Heap->BodyHeap->Size; i++)
    {
        if (Heap->BodyHeap->Memory[i] == Value)
        {
            int retValue = VecRemoveByIndex(Heap->BodyHeap, i);

            if (retValue == -1)
            {
                return -1;
            }
            valueForReturn = 0;
        }
    }
    bottomup(Heap);
    return valueForReturn;
}

int HpGetExtreme(CC_HEAP *Heap, int* ExtremeValue)
{
    if (Heap == NULL)
    {
        return -1;
    }

    *ExtremeValue = Heap->BodyHeap->Memory[0];
    return 0;

}

int HpPopExtreme(CC_HEAP *Heap, int* ExtremeValue)
{
    if (Heap == NULL)
    {
        return -1;
    }

    *ExtremeValue = Heap->BodyHeap->Memory[0];

    HpRemove(Heap, *ExtremeValue);

    return 0;
}

int HpGetElementCount(CC_HEAP *Heap)
{   
    if (Heap == NULL)
    {
        return 0;
    }

    return Heap->BodyHeap->Size;
}

int HpSortToVector(CC_HEAP *Heap, CC_VECTOR* SortedVector)
{
    if (Heap == NULL)
    {
        return -1;
    }

    if (SortedVector == NULL)
    {
        int retValue = VecCreate(&SortedVector);
        if (retValue == -1)
        {
            return -1;
        }
    }

    int popValue = 0;
    if (Heap->HeapType == 1) {
        while (Heap->BodyHeap->Size != 0)
        {
            HpGetExtreme(Heap, &popValue);
            VecInsertHead(SortedVector, popValue);
            VecRemoveByIndex(Heap->BodyHeap, 0);
            bottomup(Heap);
        }
    }
    else
    {
        while (Heap->BodyHeap->Size != 0)
        {
            HpGetExtreme(Heap, &popValue);
            VecInsertTail(SortedVector, popValue);
            VecRemoveByIndex(Heap->BodyHeap, 0);
            bottomup(Heap);
        }
    }

    return 0;
}