#include "cctree.h"
#include "common.h"

int gCounter = 0;

CC_TREE_NODE* GetNewTreeNode(int Key)
{
    CC_TREE_NODE *node = (CC_TREE_NODE*)malloc(sizeof(CC_TREE_NODE));
    node->Key = Key;
    node->Left = NULL;
    node->Right = NULL;
    node->Height = 1;
    return node;
}


int NodeGetHeight(CC_TREE_NODE *Tree)
{
    if (Tree == NULL)
    {
        return -1;
    }
    else
    {
        return 1 + max(NodeGetHeight(Tree->Left), NodeGetHeight(Tree->Right));
    }
}

int isAvl(CC_TREE_NODE* Tree)
{
    if (Tree == NULL)
    {
        return 0;
    }
    return NodeGetHeight(Tree->Left) - NodeGetHeight(Tree->Right);
}

void RotateRight(CC_TREE_NODE **Tree) {
    CC_TREE_NODE *node = (*Tree)->Left;
    CC_TREE_NODE *node1 = node->Right;
    node->Right = *Tree;
    (*Tree)->Left = node1;

    (*Tree) = node;
    (*Tree)->Height = NodeGetHeight(*Tree);
    node->Height = NodeGetHeight(node);
}

void RotateLeft(CC_TREE_NODE **Tree) {
    CC_TREE_NODE *node = (*Tree)->Right;
    CC_TREE_NODE *node1 = node->Left;
    node->Left = *Tree;
    (*Tree)->Right = node1;

    (*Tree) = node;
    (*Tree)->Height = NodeGetHeight(*Tree);
    node->Height = NodeGetHeight(node);
}

int TreeCreate(CC_TREE **Tree)
{
    if (Tree == NULL)
    {
        return -1;
    }
    *Tree = (CC_TREE*)malloc(sizeof(CC_TREE));

    if (*Tree == NULL)
    {
        return -1;
    }
    (*Tree)->Root = NULL;
    return 0;
}

int DestroyNode(CC_TREE_NODE **Tree)
{
    if (Tree == NULL)
    {
        return -1;
    }

    if (*Tree == NULL)
    {
        return -1;
    }

    DestroyNode(&(*Tree)->Left);
    DestroyNode(&(*Tree)->Right);
    free(*Tree);
    *Tree = NULL;
    return 0;
}

int TreeDestroy(CC_TREE **Tree)
{
    if (Tree == NULL)
    {
        return -1;
    }

    if (*Tree == NULL)
    {
        return -1;
    }

    DestroyNode(&(*Tree)->Root);
    free(*Tree);
    *Tree = NULL;
    return 0;
}


void InsertNode(CC_TREE_NODE **Tree, int Value)
{
    if (Tree == NULL)
    {
        return;
    }

    if (*Tree == NULL)
    {
        *Tree = GetNewTreeNode(Value);
    }

    else if (Value <= (*Tree)->Key)
    {
        InsertNode(&(*Tree)->Left, Value);
    }
    else if (Value > (*Tree)->Key)
    {
        InsertNode(&(*Tree)->Right, Value);
    }

    (*Tree)->Height = NodeGetHeight(*Tree);
    int balance = isAvl(*Tree);

    if (balance > 1 && Value < (*Tree)->Left->Key)
    {
        RotateRight(&(*Tree));
    }

    if (balance < -1 && Value >(*Tree)->Right->Key)
    {
        RotateLeft(&(*Tree));
    }

    if (balance > 1 && Value > (*Tree)->Left->Key)
    {
        RotateLeft(&(*Tree)->Left);
        RotateRight(&(*Tree));
    }

    if (balance < -1 && Value < (*Tree)->Right->Key)
    {
        RotateRight(&((*Tree)->Right));
        RotateLeft(&(*Tree));
    }
}

int TreeInsert(CC_TREE *Tree, int Value)
{
    if (Tree == NULL)
    {
        return -1;
    }
    InsertNode(&(Tree->Root), Value);
    return 0;
}

CC_TREE_NODE *FindMin(CC_TREE_NODE *Tree)
{
    if (Tree == NULL)
    {
        return NULL;
    }
    if (Tree->Left == NULL)
    {
        return Tree;
    }
    return FindMin(Tree->Left);
}

CC_TREE_NODE *FindMax(CC_TREE_NODE *Tree)
{
    if (Tree == NULL)
    {
        return NULL;
    }
    if (Tree->Right == NULL)
    {
        return Tree;
    }
    return FindMin(Tree->Right);
}

CC_TREE_NODE *GetPredecessor(CC_TREE_NODE *Tree)
{
    if (Tree == NULL)
    {
        return NULL;
    }
    if (Tree->Left == NULL)
    {
        return Tree;
    }
    return FindMax(Tree->Left);
}

CC_TREE_NODE *GetSuccessor(CC_TREE_NODE *Tree)
{
    if (Tree == NULL)
    {
        return NULL;
    }
    if (Tree->Right == NULL)
    {
        return Tree;
    }
    return FindMin(Tree->Right);
}


int NumberOfChild(CC_TREE_NODE* Tree)
{
    if (Tree == NULL)
    {
        return -1;
    }

    int cnt = 0;
    if (Tree->Left != NULL)
    {
        cnt++;
    }
    if (Tree->Right != NULL)
    {
        cnt++;
    }
    return cnt;
}

CC_TREE_NODE* TreeRemoveNode(CC_TREE_NODE *Tree, int Value)
{
    if (Tree == NULL)
    {
        return NULL;
    }
    if (Value < Tree->Key)
    {
        Tree->Left = TreeRemoveNode(Tree->Left, Value);
    }
    else if (Value > Tree->Key)
    {
        Tree->Right = TreeRemoveNode(Tree->Right, Value);
    }
    else
    {
        if (NumberOfChild(Tree) == 0)
        {
            free(Tree);
            Tree = NULL;
        }
        else if (NumberOfChild(Tree) == 1)
        {
            if (Tree->Left == NULL)
            {
                CC_TREE_NODE *temp = Tree;
                Tree = Tree->Right;
                free(temp);
                temp = NULL;
            }
            else
            {
                CC_TREE_NODE *temp = Tree;
                Tree = Tree->Left;
                free(temp);
                temp = NULL;
            }
        }
        else
        {
            CC_TREE_NODE *temp = GetSuccessor(Tree);
            Tree->Key = temp->Key;
            Tree->Right = TreeRemoveNode(Tree->Right, temp->Key);
        }
    }
    return Tree;
}

int TreeRemove(CC_TREE *Tree, int Value)
{
    if (Tree == NULL)
    {
        return -1;
    }

    if (TreeContains(Tree, Value) == 0)
    {
        return -1;
    }

    while (TreeContains(Tree, Value))
    {
        Tree->Root = TreeRemoveNode(Tree->Root, Value);
    }

    return 0;
}

int NodeContains(CC_TREE_NODE *Tree, int Value)
{
    if (Tree == NULL)
    {
        return 0;
    }

    if (Tree->Key == Value)
    {
        return 1;
    }

    if (Value <= Tree->Key)
    {
        return NodeContains(Tree->Left, Value);
    }
    else
    {
        return NodeContains(Tree->Right, Value);
    }
}

int TreeContains(CC_TREE *Tree, int Value) 
{
    if (Tree == NULL)
    {
        return -1;
    }
    return NodeContains(Tree->Root, Value);
}

int NodeGetCount(CC_TREE_NODE *Tree)
{
    int cnt = 1;
    if (Tree == NULL)
    {
        return 0;
    }
    else
    {
        cnt += NodeGetCount(Tree->Left);
        cnt += NodeGetCount(Tree->Right);
        return cnt;
    }
}

int TreeGetCount(CC_TREE *Tree) 
{
    if (Tree == NULL)
    {
        return -1;
    }
    return NodeGetCount(Tree->Root);
}

int TreeGetHeight(CC_TREE *Tree)
{
    if (Tree == NULL)
    {
        return -1;
    }
    return NodeGetHeight(Tree->Root);
}

int TreeClear(CC_TREE *Tree)
{
    if (Tree == NULL)
    {
        return -1;
    }
    TreeDestroy(&Tree);
    TreeCreate(&Tree);
    return 0;
}

int NodeGetNthPreorder(CC_TREE_NODE *Tree, int Index, int *Value)
{
    if (Tree == NULL)
    {
        return -1;
    }

    if (Index < 0)
    {
        return -1;
    }

    if (gCounter <= Index)
    {
        gCounter++;
        if (gCounter - 1 == Index)
        {
            *Value = Tree->Key;
            return 0;
        }
        NodeGetNthPreorder(Tree->Left, Index, Value);
        NodeGetNthPreorder(Tree->Right, Index, Value);
    }
    return 0;
}

int TreeGetNthPreorder(CC_TREE *Tree, int Index, int *Value)
{
    if (Tree == NULL)
    {
        return -1;
    }

    if (Index < 0 || Index >= TreeGetCount(Tree))
    {
        return -1;
    }

    int retValue = NodeGetNthPreorder(Tree->Root, Index, Value);
    gCounter = 0;
    return retValue;
}

int NodeGetNthInorder(CC_TREE_NODE *Tree, int Index, int *Value)
{
    if (Tree == NULL)
    {
        return -1;
    }

    if (Index < 0)
    {
        return -1;
    }

    if (gCounter <= Index)
    {
        NodeGetNthInorder(Tree->Left, Index, Value);
        gCounter++;
        if (gCounter - 1 == Index)
        {
            *Value = Tree->Key;
            return 0;
        }

        NodeGetNthInorder(Tree->Right, Index, Value);
    }
    return 0;
}

int TreeGetNthInorder(CC_TREE *Tree, int Index, int *Value)
{
    if (Tree == NULL)
    {
        return -1;
    }

    if (Index < 0 || Index >= TreeGetCount(Tree))
    {
        return -1;
    }

    int retValue = NodeGetNthInorder(Tree->Root, Index, Value);
    gCounter = 0;
    return retValue;
}

int NodeGetNthPostorder(CC_TREE_NODE *Tree, int Index, int *Value)
{
    if (Tree == NULL)
    {
        return -1;
    }

    if (Index < 0)
    {
        return -1;
    }

    if (gCounter <= Index)
    {
        NodeGetNthPostorder(Tree->Left, Index, Value);
        NodeGetNthPostorder(Tree->Right, Index, Value);
        gCounter++;
        if (gCounter - 1 == Index)
        {
            *Value = Tree->Key;
            return 0;
        }
    }
    return 0;
}

int TreeGetNthPostorder(CC_TREE *Tree, int Index, int *Value)
{
    if (Tree == NULL)
    {
        return -1;
    }

    if (Index < 0 || Index >= TreeGetCount(Tree))
    {
        return -1;
    }

    int retValue = NodeGetNthPostorder(Tree->Root, Index, Value);
    gCounter = 0;
    return retValue;
}

