#pragma once
#include <stdio.h>
#include <stdlib.h>

typedef struct _CC_TREE_NODE {
    int Key;
    struct  _CC_TREE_NODE *Left;
    struct  _CC_TREE_NODE *Right;
    int Height;
} CC_TREE_NODE;

typedef struct _CC_TREE {
    struct  _CC_TREE_NODE *Root;
} CC_TREE;

int TreeCreate(CC_TREE **Tree);
int TreeDestroy(CC_TREE **Tree);

// Duplicates are allowed
int TreeInsert(CC_TREE *Tree, int Value);

// Removes all elements equal to Value
int TreeRemove(CC_TREE *Tree, int Value);

/*
 *  Return:
 *      1   - Tree contains value
 *      0   - Tree does not contain Value
 */

 //verificam daca arborele e BST

int TreeContains(CC_TREE *Tree, int Value);

int TreeGetCount(CC_TREE *Tree);
int TreeGetHeight(CC_TREE *Tree);
int TreeClear(CC_TREE *Tree);

int TreeGetNthPreorder(CC_TREE *Tree, int Index, int *Value); //left->root->right
int TreeGetNthInorder(CC_TREE *Tree, int Index, int *Value);
int TreeGetNthPostorder(CC_TREE *Tree, int Index, int *Value);
