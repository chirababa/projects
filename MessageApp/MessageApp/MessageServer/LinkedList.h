
#ifndef _LINKEDLIST_H_
#define _LINKEDLIST_H_

#include "server_data.h"

void AddFirst(CLIENT_INFO *Client, CLIENT_INFO** List);
void RemoveByClient(CLIENT_INFO* Client, CLIENT_INFO** List);
void UpdateClient(CLIENT_INFO* Info, const char * Username, CLIENT_INFO* List, BOOLEAN ClearFlag);
BOOLEAN UserAlreadyLoggedIn(const char* Username, CLIENT_INFO* List);
void PrintLoggedInClients(CLIENT_INFO* List,char** ClientList);

#endif