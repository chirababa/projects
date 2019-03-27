#pragma once

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct _CC_LINKED_NODE {
    char *Key;
    int Value;
    struct _CC_LINKED_NODE *Next;
    struct _CC_LINKED_NODE *Prev;
} CC_LINKED_NODE;

typedef struct _CC_LINKED_LIST {
     CC_LINKED_NODE *Head;
     CC_LINKED_NODE *Tail;
} CC_LINKED_LIST;


CC_LINKED_NODE* GetNewNode(char* Key, int Value);
int CreateList(CC_LINKED_LIST** List);
int InsertFirst(CC_LINKED_LIST** List, char* Key, int Value);
int RemoveFirst(CC_LINKED_LIST** List);
int RemoveLast(CC_LINKED_LIST** List);
int RemoveNode(CC_LINKED_LIST** List, char* Key);
CC_LINKED_NODE* SearchKey(CC_LINKED_LIST* List, char* Key);


