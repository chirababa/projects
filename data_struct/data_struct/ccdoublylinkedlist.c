#include "ccdoublylinkedlist.h"
#include "common.h"

CC_LINKED_NODE* GetNewNode(char* Key, int Value)
{
    CC_LINKED_NODE *node = (CC_LINKED_NODE*)malloc(sizeof(CC_LINKED_NODE));
    node->Value = Value;
    node->Key = (char*)malloc((strlen(Key) + 1) * sizeof(char));
    memcpy(node->Key, Key, (strlen(Key) + 1) * sizeof(char));
    node->Next = NULL;
    node->Prev = NULL;
    return node;
}

int CreateList(CC_LINKED_LIST** List)
{
    if (List == NULL)
    {
        return -1;
    }
    *List = (CC_LINKED_LIST*)malloc(sizeof(CC_LINKED_LIST));
    if (*List == NULL)
    {
        return -1;
    }
    (*List)->Head = NULL;
    (*List)->Tail = NULL;
    return 0;
}

int InsertFirst(CC_LINKED_LIST** List, char* Key, int Value)
{
    if (List == NULL)
    {
        return -1;
    }
    CC_LINKED_NODE* node = GetNewNode(Key, Value);
    if ((*List)->Head == NULL)
    {
        (*List)->Head = node;
        (*List)->Tail = node;
    }
    else
    {
        (*List)->Head->Prev = node;
        node->Next = (*List)->Head;
        (*List)->Head = node;
    }
    return 0;
}

int RemoveFirst(CC_LINKED_LIST** List)
{
    if (List == NULL)
    {
        return -1;
    }
    if ((*List)->Head->Next == NULL)
    {
        free((*List)->Head);
        (*List)->Head = NULL;
        (*List)->Tail = NULL;
    }
    else
    {
        (*List)->Head->Next->Prev = NULL;
        (*List)->Head = (*List)->Head->Next;
    }
    return 0;
}

int RemoveLast(CC_LINKED_LIST** List)
{
    if (List == NULL)
    {
        return -1;
    }
    if ((*List)->Tail->Prev == NULL)
    {
        free((*List)->Tail);
        (*List)->Tail = NULL;
        (*List)->Head = NULL;
    }
    else
    {
        (*List)->Tail->Prev->Next = NULL;
        (*List)->Tail = (*List)->Tail->Prev;
    }
    return 0;
}

int RemoveNode(CC_LINKED_LIST** List, char* Key)
{
    if (List == NULL)
    {
        return -1;
    }

    for (CC_LINKED_NODE *node = (*List)->Head; node != NULL; node = node->Next)
    {
        if (!strcmp(node->Key, Key))
        {
            if (node == (*List)->Head)
            {
                RemoveFirst(List);
                return 0;
            }
            else if (node == (*List)->Tail)
            {
                RemoveLast(List);
                return 0;
            }
            node->Next->Prev = node->Prev;
            node->Prev->Next = node->Next;
            free(node);
            node = NULL;
            return 0;
        }
    }
    return 0;
}

CC_LINKED_NODE* SearchKey(CC_LINKED_LIST* List, char* Key) 
{
    if (List == NULL)
    {
        return NULL;
    }

    for (CC_LINKED_NODE *node = List->Head; node != NULL; node = node->Next)
    {
        if (!strcmp(node->Key, Key))
        {
            return node;
        }
    }
    return NULL;
}
