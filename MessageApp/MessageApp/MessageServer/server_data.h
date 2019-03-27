#ifndef _SERVER_DATA_H_
#define _SERVER_DATA_H_

#include "communication_api.h"

#define CLIENT_INFO_MAX_SIZE 256
#define MAX_CONNECTIONS 512
#define BUFFER_LIST_CLIENTS 4096
#define FILE_PATH "C:\\registration.txt"
#define FILE_HISTORY_PATH "history.txt"
#define FILE_OFFLINE_MESSAGE_PATH "offlineMessage.txt"

typedef struct _CLIENT_INFO 
{
	CM_SERVER_CLIENT *Client;
	BOOLEAN IsConnected;
	//HANDLE ThreadHandle;
	struct _CLIENT_INFO *Next;
    DWORD UsernameSize;
#pragma warning(suppress : 4200)
	BYTE Username[0];
}CLIENT_INFO, *PCLIENT_INFO;

#endif