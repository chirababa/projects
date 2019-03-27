
#include "stdafx.h"
#include <windows.h>
#include <string.h>
#include "LinkedList.h"

void AddFirst(CLIENT_INFO *Client, CLIENT_INFO** List)
{
    CLIENT_INFO* newNode = NULL;
    newNode = (PCLIENT_INFO)malloc(sizeof(CLIENT_INFO)+ CLIENT_INFO_MAX_SIZE);
    memset(newNode, 0, sizeof(CLIENT_INFO) + CLIENT_INFO_MAX_SIZE);
    newNode->Client = Client->Client;
    newNode->IsConnected = FALSE;
    newNode->Next = *List;
    *List = newNode;
}

void RemoveByClient(CLIENT_INFO* Client, CLIENT_INFO ** List)
{
    CLIENT_INFO *temp=*List;
    CLIENT_INFO *aux=NULL;
    if (*List == NULL)
    {
        return;
    }
   
    if (temp->Client == Client->Client)
    {
        *List = temp->Next;
        temp = NULL;
        free(temp);
        return;
    }


    while (temp->Next->Client != Client->Client)
    {
        temp = temp->Next;
    }

    aux = temp->Next;
    temp->Next = aux->Next;
    aux = NULL;
    free(aux);

}

void UpdateClient(CLIENT_INFO* Info,const char * Username, CLIENT_INFO* List, BOOLEAN ClearFlag)
{
    CLIENT_INFO* temp = List;

    if (List == NULL)
    {
        return;
    }

    while (temp->Client != Info->Client)
    {
        temp = temp->Next;
    }

    if (ClearFlag == FALSE)
    {
        temp->IsConnected = TRUE;
        Info->IsConnected = TRUE;
        memcpy(temp->Username,(PBYTE)Username, strlen(Username) * sizeof(char) + 1);
        memcpy(Info->Username, (PBYTE)Username, strlen(Username) * sizeof(char) + 1);
    }
    else
    {
        temp->IsConnected = FALSE;
        Info->IsConnected = FALSE;
        memset((char*)temp->Username, 0, CLIENT_INFO_MAX_SIZE);
        memset((char*)Info->Username, 0, CLIENT_INFO_MAX_SIZE);
    }
}

BOOLEAN UserAlreadyLoggedIn(const char * Username, CLIENT_INFO * List)
{
    while(List!=NULL)
    {
        if (List->IsConnected == TRUE)
        {
            if (strncmp(Username, (char*)List->Username, strlen(Username) * sizeof(char)) == 0)
            {
                return TRUE;
            }
        }
        List = List->Next;
    }
    return FALSE;
}

void PrintLoggedInClients(CLIENT_INFO * List,char** ClientList)
{
    CLIENT_INFO *temp = List;
    while (temp != NULL)
    {
        if (temp->IsConnected == TRUE)
        {
            strncat(*ClientList, (char*)temp->Username, strlen((char*)temp->Username));
            strncat(*ClientList, "\n",1);
        }
        temp = temp->Next;
    }
    memset(*ClientList + (strlen(*ClientList) - 1), 0, 1);
}
