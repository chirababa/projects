#include "cchashtable.h"
#include "common.h"

size_t HashFunction(char *Key)
{
    if (Key == NULL)
    {
        return 0;
    }

    size_t hash = 0;
    while (*Key)
    {
        char c = *Key++;
        int a = c - '0';
        hash = (hash * 5)+ a;
    }
    return hash;
}

int HtCreate(CC_HASH_TABLE** HashTable)
{
    if (HashTable == NULL)
    {
        return -1;
    }

    *HashTable = (CC_HASH_TABLE*)malloc(sizeof(CC_HASH_TABLE));
    if (*HashTable == NULL)
    {
        return -1;
    }
    
    (*HashTable)->ArrayHashTable = (CC_LINKED_LIST**)malloc(HASH_SIZE * sizeof(CC_LINKED_LIST*));
    if ((*HashTable)->ArrayHashTable == NULL)
    {
        free(*HashTable);
        *HashTable = NULL;
        return -1;
    }

    (*HashTable)->CountBucketElements = (int*)malloc(HASH_SIZE * sizeof(int));
    if ((*HashTable)->CountBucketElements == NULL)
    {
        free((*HashTable)->ArrayHashTable);
        (*HashTable)->ArrayHashTable = NULL;
        free(*HashTable);
        *HashTable = NULL;
        return -1;
    }

    for (int i = 0; i < HASH_SIZE; i++)
    {
        (*HashTable)->CountBucketElements[i] = 0;
        CreateList(&((*HashTable)->ArrayHashTable[i]));
    }

    return 0;
}

int HtDestroy(CC_HASH_TABLE** HashTable)
{
    if (HashTable == NULL)
    {
        return -1;
    }

    if (*HashTable == NULL)
    {
        return -1;
    }

    if ((*HashTable)->ArrayHashTable!=NULL)
    {
        free((*HashTable)->ArrayHashTable);
        (*HashTable)->ArrayHashTable=NULL;
    }

    if ((*HashTable)->CountBucketElements != NULL)
    {
        free((*HashTable)->CountBucketElements);
        (*HashTable)->CountBucketElements = NULL;
    }

    free(*HashTable);
    *HashTable = NULL;
    return 0;
}

int HtSetKeyValue(CC_HASH_TABLE* HashTable, char* Key, int Value)
{
    if (HashTable == NULL)
    {
        return -1;
    }
    size_t bucketIndex = HashFunction(Key) % HASH_SIZE;

    CC_LINKED_NODE *node = SearchKey(HashTable->ArrayHashTable[bucketIndex], Key);

    if (node != NULL)
    {
        return -1;
    }

    int retValue = InsertFirst(&((HashTable)->ArrayHashTable[bucketIndex]), Key, Value);
    if (retValue == -1)
    {
        return -1;
    }
    (HashTable->CountBucketElements[bucketIndex])++;
    return 0;
}

int HtGetKeyValue(CC_HASH_TABLE* HashTable, char* Key, int *Value)
{
    if (HashTable == NULL)
    {
        return -1;
    }

    size_t bucketIndex = HashFunction(Key) % HASH_SIZE;
    CC_LINKED_NODE* node = SearchKey(HashTable->ArrayHashTable[bucketIndex], Key);
    if (node == NULL)
    {
        return -1;
    }
    *Value = node->Value;
    return 0;
}

int HtRemoveKey(CC_HASH_TABLE* HashTable, char* Key)
{
    if (HashTable == NULL)
    {
        return -1;
    }

    size_t bucketIndex = HashFunction(Key) % HASH_SIZE;
    CC_LINKED_NODE* node = SearchKey(HashTable->ArrayHashTable[bucketIndex], Key);

    if (node == NULL)
    {
        return -1;
    }

    int retValue=RemoveNode(&(HashTable->ArrayHashTable[bucketIndex]), Key);

    if (retValue == -1)
    {
        return -1;
    }
    return 0;
}

int HtHasKey(CC_HASH_TABLE* HashTable, char* Key)
{
    if (HashTable == NULL)
    {
        return -1;
    }

    size_t bucketIndex = HashFunction(Key) % HASH_SIZE;

    CC_LINKED_NODE* node = SearchKey(HashTable->ArrayHashTable[bucketIndex], Key);
    if (node != NULL)
    {
        return 1;
    }

    return 0;
}

int HtGetNthKey(CC_HASH_TABLE* HashTable, int Index, char** Key)
{
    if (HashTable == NULL)
    {
        return -1;
    }

    if ((Index > HASH_SIZE - 1) || Index < 0)
    {
        return -1;
    }

    if (Index >= HtGetKeyCount(HashTable))
    {
        return -1;
    }

    int countBucket = 0;
    int bucket = 0;
    int indexInBucket = 0;
    int countNode = 0;
    for (int i = 0; i < HASH_SIZE; i++)
    {
        countBucket += (HashTable->CountBucketElements[i]);
        if (Index < countBucket)
        {
            bucket = i;
            break;
        }
    }

    indexInBucket = Index - countBucket + (HashTable->CountBucketElements[bucket]);

    for (CC_LINKED_NODE *node = HashTable->ArrayHashTable[bucket]->Head; node != NULL; node = node->Next)
    {
        if (countNode == indexInBucket) {
            *Key = (char*)malloc((strlen(node->Key) + 1) * sizeof(char));
            memcpy(*Key, node->Key, (strlen(node->Key) + 1) * sizeof(char));
            return 1;
        }
        countNode++;
    }
    return 0;
}

int HtClear(CC_HASH_TABLE* HashTable)
{
    if (HashTable == NULL)
    {
        return -1;
    }
    HtDestroy(&HashTable);
    HtCreate(&HashTable);
    return 0;
}

int HtGetKeyCount(CC_HASH_TABLE* HashTable)
{
    if (HashTable == NULL)
    {
        return -1;
    }

    int sum = 0;
    for (int i = 0; i < HASH_SIZE; i++)
    {
        sum += (HashTable->CountBucketElements[i]);
    }
    return sum;
}
