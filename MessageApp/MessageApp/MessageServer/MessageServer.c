// MessageServer.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"

// communication library
#include <windows.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include "common_structure.h"
#include "server_data.h"
#include "LinkedList.h"

PBYTE gFileEnd;
PBYTE gFileStart;
PBYTE gFileHistoryStart;
PBYTE gFileHistoryEnd;
PBYTE gFileOfflineMessageStart;
PBYTE gFileOfflineMessageEnd;
DWORD gNrConnections;
HANDLE *gThreadPool=NULL;
HANDLE *ghMutex, *ghMutexFile, *ghMutexFileHistory, *ghMutexFileOfflineMessage;
CLIENT_INFO *gListHead = NULL;
CM_SERVER* server = NULL;
DWORD gMaxNumberOfClients;

RESPONSE_SERVER_TYPE VerifyExistingUsername(COMMAND_TYPE Command, const char* Username, const char* Password)
{
    int dataSize = 0;
    int min = 0;
    PBYTE pLoginData = gFileStart;
    PBYTE pUsername = pLoginData;
    int stringEqual = -1;
    while ((*pLoginData) != 0)
    {
        if ((*pLoginData) == ',')
        {
            min = MIN(dataSize, (int)strlen(Username));
            if (dataSize - strlen(Username) == 0)
            {
                stringEqual = strncmp((char*)pUsername, Username, min * sizeof(char));
                if (Command == CMD_REGISTER && stringEqual == 0)
                {
                    return USERNAME_ALREADY_REGISTERED;
                }
            }
            pUsername = pLoginData + 1;
            dataSize = -1;
        }
        if ((*pLoginData) == '\n')
        {
            min = MIN(dataSize, (int)strlen(Password));
            if (dataSize - strlen(Password) == 0)
            {
                if (stringEqual == 0 && strncmp((char*)pUsername, Password, min * sizeof(char)) == 0)
                {
                    return USERNAME_ALREADY_REGISTERED;
                }
                 stringEqual = -1;
            }
            pUsername = pLoginData + 1;
            dataSize = -1;
        }
        pLoginData++;
        dataSize++;
    }
    return RESPONSE_SUCCES;
}

RESPONSE_SERVER_TYPE WriteDataForRegister(PREGISTER Info)
{
    char* username = (char*)malloc((Info->UsernameLength + 1) * sizeof(char));
    char* password = (char*)malloc((Info->PasswordLength + 1) * sizeof(char));
    memcpy(password, Info->FieldAccess + Info->UsernameLength + 1, Info->PasswordLength * sizeof(char));
    memcpy(username, Info->FieldAccess, Info->UsernameLength * sizeof(char));
    memset(password + Info->PasswordLength, 0, 1);
    memset(username + Info->UsernameLength, 0, 1);

    for (int i = 0; i < strlen(username); i++)
    {
        if (isalnum(username[i]) == 0)
        {
            return INVALID_USERNAME;
        }
    }

    for (int i = 0; i < strlen(password); i++)
    {
        if (password[i] == ',' || password[i] == ' ')
        {
            return INVALID_PASSWORD;
        }
    }
    BOOL passwordContainsUppercase = FALSE;
    BOOL passwordContainsNonalpha = FALSE;

    for (int i = 0; i < strlen(password); i++)
    {
        if (isupper(password[i]) != 0)
        {
            passwordContainsUppercase = TRUE;
        }
        if (isalnum(password[i]) == 0)
        {
            passwordContainsNonalpha = TRUE;
        }
    }

    if (passwordContainsNonalpha == FALSE || passwordContainsUppercase == FALSE || strlen(password) < 5)
    {
        return PASSWORD_TOO_WEAK;
    }

    if (VerifyExistingUsername(CMD_REGISTER,username, password) == RESPONSE_SUCCES)
    {
        strcat(strcat(strcat(username, ","), password), "\n");
        memcpy(gFileEnd, username, strlen(username) * sizeof(char));
        gFileEnd = gFileEnd + strlen(username);
        return RESPONSE_SUCCES;
    }
    return USERNAME_ALREADY_REGISTERED;
}

void ParseUsernamePassword(PREGISTER Info, char** Username, char** Password)
{
    *Username = (char*)malloc((Info->UsernameLength + 1) * sizeof(char));
    *Password = (char*)malloc((Info->PasswordLength + 1) * sizeof(char));
    memcpy(*Password, Info->FieldAccess + Info->UsernameLength + 1, Info->PasswordLength * sizeof(char));
    memcpy(*Username, Info->FieldAccess, Info->UsernameLength * sizeof(char));
    memset(*Password + Info->PasswordLength, 0, 1);
    memset(*Username + Info->UsernameLength, 0, 1);
}

void ParseDataForHistory(PHISTORY Info, char** Username, DWORD* MessageCounter)
{
    *Username = (char*)malloc((Info->UsernameLength + 1) * sizeof(char));
    *MessageCounter = Info->MessageCounter;
    memcpy(*Username, Info->FieldAccess, (Info->UsernameLength) * sizeof(char));
    memset(*Username + Info->UsernameLength, 0, 1);
}

RESPONSE_SERVER_TYPE ReadDataForLogin(PREGISTER Info)
{
    char *username = NULL;
    char *password = NULL;
    ParseUsernamePassword(Info,&username,&password);
    if (VerifyExistingUsername(CMD_LOGIN,username, password) == USERNAME_ALREADY_REGISTERED)
    {
        return RESPONSE_SUCCES;
    }
    return INVALID_USERNAME;
}

CM_ERROR CreateBufferForResponse(COMMAND_TYPE Command, const char* Info, CM_SERVER_CLIENT* Client)
{
    CM_ERROR error = CM_SUCCESS;
    PCLIENT_DATA clientData = NULL;
    CM_DATA_BUFFER* dataToSend = NULL;
    PRESPONSE_FROM_SERVER pResponse = NULL;
    CM_SIZE sendBytesCount = 0;
    CM_SIZE bufferSize = sizeof(CLIENT_DATA) + sizeof(PRESPONSE_FROM_SERVER) + BUFFER_SIZE;
    clientData = (PCLIENT_DATA)malloc(bufferSize);

    if (clientData == NULL)
    {
        return CM_NO_MEMORY;
    }

    clientData->Comand = Command;
    pResponse = (PRESPONSE_FROM_SERVER)clientData->Buffer;
    memcpy(pResponse->ServerResponse, (PBYTE)Info, strlen(Info) * sizeof(char) + 1);
    
    error = CreateDataBuffer(&dataToSend, bufferSize);
    if (CM_IS_ERROR(error))
    {
        goto cleanup;
    }

    error = CopyDataIntoBuffer(dataToSend, (CM_BYTE*)clientData, bufferSize);
    if (CM_IS_ERROR(error))
    {
        goto cleanup;
    }

    error = SendDataToClient(Client, dataToSend, &sendBytesCount);
    if (CM_IS_ERROR(error))
    {
        goto cleanup;
    }

 cleanup:
      if (pResponse != NULL)
      {
          DestroyDataBuffer(dataToSend);
          free(clientData);
      }

    return error;
}

CM_ERROR SendToClientForEchoCommand(CM_SERVER_CLIENT* Client, CM_DATA_BUFFER* DataToReceive)
{
    CM_ERROR error = CM_SUCCESS;
    CM_SIZE successfullySendBytesCount = 0;
    error = SendDataToClient(Client, DataToReceive, &successfullySendBytesCount);
    return error;
}

CM_ERROR SendDataToClientForList(const char* Info, CM_SERVER_CLIENT* Client)
{
    CM_ERROR error = CM_SUCCESS;
    PCLIENT_DATA clientData = NULL;
    CM_DATA_BUFFER* dataToSend = NULL;
    PLIST pResponse = NULL;
    CM_SIZE sendBytesCount = 0;
    CM_SIZE bufferSize = sizeof(CLIENT_DATA) + sizeof(PLIST) + BUFFER_LIST_CLIENTS;
    clientData = (PCLIENT_DATA)malloc(bufferSize);

    if (clientData == NULL)
    {
        return CM_NO_MEMORY;
    }

    clientData->Comand = CMD_LIST;
    pResponse = (PLIST)clientData->Buffer;
    memcpy(pResponse->FieldAccess, (PBYTE)Info, strlen(Info) * sizeof(char) + 1);

    error = CreateDataBuffer(&dataToSend, bufferSize);
    if (CM_IS_ERROR(error))
    {
        goto cleanup;
    }

    error = CopyDataIntoBuffer(dataToSend, (CM_BYTE*)clientData, bufferSize);
    if (CM_IS_ERROR(error))
    {
        goto cleanup;
    }

    error = SendDataToClient(Client, dataToSend, &sendBytesCount);
    if (CM_IS_ERROR(error))
    {
        goto cleanup;
    }

cleanup:
    if (pResponse != NULL)
    {
        DestroyDataBuffer(dataToSend);
        free(clientData);
    }

    return error;
}

CM_ERROR SendOfflineMessageToClient(CLIENT_INFO* Client, const char* Username)
{
    (void)Client;
    CM_ERROR error = CM_SUCCESS;
    char* messageBuffer = (char*)calloc(BUFFER_SIZE, sizeof(char));
    int dataSize = 0;
    PBYTE pLoginData = gFileOfflineMessageStart;
    PBYTE pUsername = pLoginData;
    PBYTE pNextMessage = pLoginData;
    BOOLEAN newLineFlag = FALSE;
    BOOLEAN sendFlag = FALSE;
    char* usernameSendClient = NULL;
    int stringEqualDest = -1;

    while ((*pLoginData) != 0)
    {
        if (*pLoginData == 't' && *(pLoginData + 1) == 'o' && *(pLoginData + 2) == ' ' && newLineFlag == FALSE)
        {
            usernameSendClient = (char*)calloc(dataSize - 5, sizeof(char));
            newLineFlag = TRUE;
            memcpy(usernameSendClient, (char*)pUsername + 5, (dataSize - 6) * sizeof(char));
        }

        if ((*pLoginData) == ':')
        {
            if (dataSize - 9 - strlen(usernameSendClient) - strlen(Username) == 0)
            {
                stringEqualDest = strncmp((char*)pUsername + dataSize - strlen(Username), Username, strlen(Username) * sizeof(char));
            }
        }
        if ((*pLoginData) == '\n')
        {
            if (stringEqualDest == 0)
            {
                strncat(messageBuffer, "Message from ", strlen("Message from ") * sizeof(char));
                strncat(messageBuffer, usernameSendClient, strlen(usernameSendClient) * sizeof(char));
                strncat(messageBuffer,((char*)pUsername) + 9 + strlen(usernameSendClient) + strlen(Username), dataSize - 8 - strlen(usernameSendClient) - strlen(Username));
                pNextMessage += dataSize + 1;
                memcpy(pUsername, pNextMessage, FILE_SIZE - (pLoginData - gFileOfflineMessageStart + 1));
                sendFlag = TRUE;
                pLoginData = pUsername;
            }
            else
            {
                pUsername = pLoginData + 1;
            }
            pNextMessage = pUsername;
            newLineFlag = FALSE;
            stringEqualDest = -1;
            dataSize = 0;
        }
        pLoginData++;
        dataSize++;
    }
    printf("Buffer:%s\n", messageBuffer);
    if (sendFlag == TRUE)
    {
        error = CreateBufferForResponse(CMD_LOGIN, messageBuffer, Client->Client);
    }
    return error;
}

CM_ERROR SynchronizeOfflineMessage(CLIENT_INFO *Client, SYNC_OPERATION Operation, const char* Buffer, size_t BufferSize)
{
    (void)Client;
    WaitForSingleObject(ghMutexFileOfflineMessage, INFINITE);
    CM_ERROR error = CM_SUCCESS;
    switch (Operation)
    {
    case WRITE_FILE:
        memcpy(gFileOfflineMessageEnd, Buffer, BufferSize);
        gFileOfflineMessageEnd +=strlen(Buffer);
        break;

    case READ_FILE:
        SendOfflineMessageToClient(Client, Buffer);
        break;
    }
    ReleaseMutex(ghMutexFileOfflineMessage);
    return error;
}

CM_ERROR SendMessageMsg(CLIENT_INFO* Client, PMSGSTRUCT Pmsgstruct)
{
    CM_ERROR error = CM_SUCCESS;
    CLIENT_INFO* node = gListHead;
    BOOLEAN offlineClient = TRUE;
    size_t bufferSize = Pmsgstruct->MessageLength + 19 + strlen((char*)Client->Username);
    size_t bufferOfflineMeessageSize = Pmsgstruct->MessageLength + strlen((char*)Client->Username) + Pmsgstruct->UsernameLength + 11;

    char* username = (char*)malloc(Pmsgstruct->UsernameLength*sizeof(char)+1);
    memcpy(username, Pmsgstruct->FieldAccess, Pmsgstruct->UsernameLength);
    memset(username + Pmsgstruct->UsernameLength , 0, 1);

    char* DataToWriteIntoFile = (char*)calloc(bufferOfflineMeessageSize, sizeof(char));
    strncat(DataToWriteIntoFile,"From ", strlen("From "));
    strncat(DataToWriteIntoFile,(char*)Client->Username, strlen((char*)Client->Username));
    strncat(DataToWriteIntoFile," to ", strlen(" to "));
    strncat(DataToWriteIntoFile,username, Pmsgstruct->UsernameLength);
    strncat(DataToWriteIntoFile,":",1);
    strncat(DataToWriteIntoFile, (char*)((Pmsgstruct->FieldAccess) + Pmsgstruct->UsernameLength + 1), Pmsgstruct->MessageLength + 1);
    strncat(DataToWriteIntoFile, "\n",1);

    char* DataTosendBuffer = (char*)calloc(bufferSize, sizeof(char));
    strncat(DataTosendBuffer, "Message from ", strlen("Message from "));
    strncat(DataTosendBuffer, (char*)Client->Username, strlen((char*)Client->Username));
    strncat(DataTosendBuffer, ":", 1);
    strncat(DataTosendBuffer, (char*)((Pmsgstruct->FieldAccess)+Pmsgstruct->UsernameLength+1),Pmsgstruct->MessageLength+1);
    strncat(DataTosendBuffer, "\n", 1);

    while (node != NULL)
    {
        if (strcmp((char*)Client->Username,(char*)node->Username)==0)
        {
            error = CreateBufferForResponse(CMD_MSG, "Success\n", Client->Client);
        }
        if(strcmp(username,(char*)node->Username) == 0)
        {
            error = CreateBufferForResponse(CMD_MSG, DataTosendBuffer, node->Client);
            offlineClient = FALSE;
        }
        node = node->Next;
    }
    if (offlineClient == TRUE)
    {
        SynchronizeOfflineMessage(Client,WRITE_FILE,DataToWriteIntoFile, bufferOfflineMeessageSize * sizeof(char));
    }
    memcpy(gFileHistoryEnd, DataToWriteIntoFile, bufferOfflineMeessageSize * sizeof(char));
    gFileHistoryEnd = gFileHistoryEnd + strlen(DataToWriteIntoFile);
    return error;
}

CM_ERROR SendMessageBroadcast(CLIENT_INFO* Client, const char* DataToSend)
{
    CM_ERROR error = CM_SUCCESS;
    CLIENT_INFO* node = gListHead;
    size_t bufferSize = strlen(DataToSend) + 20 + strlen((char*)Client->Username);
    char* DataTosendBuffer = (char*)calloc(bufferSize, sizeof(char));
    strncat(DataTosendBuffer, "Broadcast from ", strlen("Broadcast from "));
    strncat(DataTosendBuffer, (char*)Client->Username, strlen((char*)Client->Username));
    strncat(DataTosendBuffer, ":",1);
    strncat(DataTosendBuffer, DataToSend, strlen(DataToSend));
    strncat(DataTosendBuffer, "\n",1);
    while (node != NULL)
    {
        if (Client->Client == node->Client)
        {
            error = CreateBufferForResponse(CMD_BROADCAST, "Success\n", Client->Client);
        }
        else
        {
            error = CreateBufferForResponse(CMD_BROADCAST, DataTosendBuffer, node->Client);
        }
        node = node->Next;
    }
    return error;
}

CM_ERROR SendHistoryBuffer(CLIENT_INFO* Client, const char* Username, DWORD Count)
{
    CM_ERROR error = CM_SUCCESS;
    char* messageBuffer = (char*)calloc(BUFFER_SIZE, sizeof(char));
    int dataSize = 0;
    DWORD countMessage = 0;
    PBYTE pLoginData = gFileHistoryStart;
    PBYTE pUsername = pLoginData;
    BOOLEAN newLineFlag = FALSE;
    int stringEqualClient = -1;
    int stringEqualDest = -1;
    int stringEqualClient1 = -1;
    int stringEqualDest1 = -1;
    while ((*pLoginData) != 0 && countMessage != Count)
    {
        if (*pLoginData == 't' && *(pLoginData + 1) == 'o' && *(pLoginData + 2) == ' ' && newLineFlag == FALSE)
        {
            if (dataSize - 6 - strlen(Username) == 0)
            {
                stringEqualDest = strncmp((char*)pUsername + 5, Username, (dataSize - 6) * sizeof(char));
            }

            if (dataSize - 6 - strlen((char*)Client->Username) == 0)
            {
                stringEqualClient = strncmp((char*)pUsername + 5, (char*)Client->Username, (dataSize - 6) * sizeof(char));
            }
            newLineFlag = TRUE;
            dataSize = 0;
            pUsername = pLoginData;
        }

        if ((*pLoginData) == ':')
        {
            if (dataSize - 3 - strlen(Username) == 0)
            {
                stringEqualDest1 = strncmp((char*)pUsername + 3, Username, (dataSize - 3) * sizeof(char));
            }

            if (dataSize - 3 - strlen((char*)Client->Username) == 0)
            {
                stringEqualClient1 = strncmp((char*)pUsername + 3, (char*)Client->Username, (dataSize - 3) * sizeof(char));
            }
            pUsername = pLoginData;
            dataSize = 0;
        }

        if ((*pLoginData) == '\n')
        {
            if (stringEqualClient1 == 0 && stringEqualDest == 0)
            {
                strncat(messageBuffer, "From ", strlen("From ") * sizeof(char));
                strncat(messageBuffer, Username, strlen(Username) * sizeof(char));
                strncat(messageBuffer, (char*)pUsername, dataSize + 1);
                countMessage++;
            }
            if (stringEqualClient == 0 && stringEqualDest1 == 0)
            {
                strncat(messageBuffer, "From ", strlen("From ") * sizeof(char));
                strncat(messageBuffer, (char*)Client->Username, strlen((char*)Client->Username) * sizeof(char));
                strncat(messageBuffer, (char*)pUsername, dataSize + 1);
                countMessage++;
            }
            if (countMessage == Count)
            {
                break;
            }
            stringEqualClient = -1;
            stringEqualDest = -1;
            stringEqualClient1 = -1;
            stringEqualDest1 = -1;
            pUsername = pLoginData + 1;
            dataSize = -1;
            newLineFlag = FALSE;
        }
        pLoginData++;
        dataSize++;
    }

    if (countMessage == 0)
    {
        memcpy(messageBuffer, "\n", 1);
    }
    strncat(messageBuffer, "\0", 1);
    error = CreateBufferForResponse(CMD_HISTORY, messageBuffer, Client->Client);
    return error;
}

RESPONSE_SERVER_TYPE SynchronizeThread(const char* Username, CLIENT_INFO *Client, SYNC_OPERATION Operation, char** ClientList, PMSGSTRUCT MsgStruct)
{
    WaitForSingleObject(ghMutex, INFINITE);
    RESPONSE_SERVER_TYPE response = RESPONSE_SUCCES;
    BOOLEAN UserIsLoggedIn = FALSE;
    CLIENT_INFO* temp = gListHead;
    switch (Operation)
    {
    case ADD_LIST:
        AddFirst(Client, &gListHead);
        gNrConnections++;
        break;

    case REMOVE_LIST:
        RemoveByClient(Client, &gListHead);
        break;

    case UPDATE_LOGGED_IN_CLIENTS:
        UpdateClient(Client, Username,gListHead,FALSE);
        while (temp != NULL)
        {
            temp = temp->Next;
        }
        break;

    case LOGOUT_UPDATE_LIST:
        UpdateClient(Client, Username,gListHead,TRUE);
        break;

    case USER_LOGGED_IN:
        UserIsLoggedIn = UserAlreadyLoggedIn(Username, gListHead);
        if (UserIsLoggedIn == TRUE)
        {
            response = USER_ALREADY_LOGGED_IN_ANOTHER_CONNECTION;
            break;
        }
        response = RESPONSE_SUCCES;
        break;

    case BROADCAST_SEND_MESSAGE:
        SendMessageBroadcast(Client, Username);
        break;

    case PRINT_CONNECTED_CLIENTS:
        PrintLoggedInClients(gListHead,ClientList);
        break;

    case EXIT_CLIENT:
        RemoveByClient(Client, &gListHead);
        //deleteThreadPOOL
        gNrConnections--;
        break;

    case MSG_SYNC:
        SendMessageMsg(Client, MsgStruct);
        break;

    default:
        break;
    }
    ReleaseMutex(ghMutex);
    return response;
}

RESPONSE_SERVER_TYPE SynchronizeFile(const char* Username, CLIENT_INFO *Client, SYNC_OPERATION Operation, PREGISTER Info)
{
    WaitForSingleObject(ghMutexFile, INFINITE);
    RESPONSE_SERVER_TYPE response = RESPONSE_SUCCES;
    switch (Operation)
    {
    case VERIFY_REGISTERED_CLIENTS:
        response = VerifyExistingUsername(CMD_REGISTER, Username, "randomString");
        break;

    case WRITE_FILE:
        response = WriteDataForRegister(Info);
        break;

    case READ_FILE:
        response = ReadDataForLogin(Info);
        if (response == RESPONSE_SUCCES && SynchronizeThread(Username, Client, USER_LOGGED_IN, NULL,NULL) == RESPONSE_SUCCES)
        {
            SynchronizeThread(Username, Client, UPDATE_LOGGED_IN_CLIENTS, NULL, NULL);
            break;
        }
        else if (response == INVALID_USERNAME)
        {
            response = INVALID_USERNAME;
            break;
        }
        response = USER_ALREADY_LOGGED_IN_ANOTHER_CONNECTION;
        break;

    default:
        break;
    }
    ReleaseMutex(ghMutexFile);
    return response;
}

CM_ERROR SynchronizeFileHistory(const char* Username, CLIENT_INFO *Client, SYNC_OPERATION Operation, DWORD MessageCounter,PMSGSTRUCT MsgData)
{
    WaitForSingleObject(ghMutexFileHistory, INFINITE);
    RESPONSE_SERVER_TYPE response = RESPONSE_SUCCES;
    CM_ERROR error = CM_SUCCESS;
    switch (Operation)
    {
    case WRITE_FILE:
        response = SynchronizeFile(Username, Client, VERIFY_REGISTERED_CLIENTS, NULL);
        if (response == USERNAME_ALREADY_REGISTERED)
        {
            SynchronizeThread(NULL, Client, MSG_SYNC, NULL, MsgData);
        }
        else
        {
           error = CreateBufferForResponse(CMD_HISTORY, "Error:No such user\n", Client->Client);
        }
        break;

    case READ_FILE:
        response = SynchronizeFile(Username, Client, VERIFY_REGISTERED_CLIENTS, NULL);
        if (response == USERNAME_ALREADY_REGISTERED)
        {
            SendHistoryBuffer(Client, Username, MessageCounter);
        }
        else
        {
            error = CreateBufferForResponse(CMD_HISTORY, "Error:No such user",Client->Client);
        }
        break;

    default:
        break;
    }
    ReleaseMutex(ghMutexFileHistory);
    return error;
}

CM_ERROR SendResponseToClient(COMMAND_TYPE Command, void* Info, CLIENT_INFO* Client,char* DataForBroadcast)
{
    CM_ERROR error = CM_SUCCESS;
    RESPONSE_SERVER_TYPE responseType = 0;
    char *username = NULL;
    char* password= NULL;
    DWORD messageCounterForHistory = 0;
    char* connectedClientsList = NULL;

    switch (Command)
    {
    case CMD_REGISTER:
        ParseUsernamePassword((PREGISTER)Info, &username, &password);
        responseType = SynchronizeFile(username, NULL, WRITE_FILE, (PREGISTER)Info);
        
        if (responseType == RESPONSE_SUCCES)
        {
            error=CreateBufferForResponse(Command,"Success\n",Client->Client);
            return error;
        }
        else if (responseType == INVALID_USERNAME)
        {
            error = CreateBufferForResponse(Command, "Error: Invalid username\n", Client->Client);
            return error;
        }
        else if (responseType == USERNAME_ALREADY_REGISTERED)
        {
            error = CreateBufferForResponse(Command, "Error: Username already registered\n", Client->Client);
            return error;
        }
        else if (responseType == INVALID_PASSWORD)
        {
            error = CreateBufferForResponse(Command, "Error: Invalid password\n", Client->Client);
            return error;
        }
        else if (responseType == PASSWORD_TOO_WEAK)
        {
            error = CreateBufferForResponse(Command, "Error: Password too weak\n", Client->Client);
            return error;
        }
        break;

    case CMD_LOGIN:
        ParseUsernamePassword((PREGISTER)Info, &username, &password);
         responseType = SynchronizeFile(username,Client, READ_FILE, (PREGISTER)Info);
        if (responseType == RESPONSE_SUCCES)
        {
            error = CreateBufferForResponse(Command, "Success\n", Client->Client);
            error = SynchronizeOfflineMessage(Client, READ_FILE, username, 0);
            return error;
        }
        else if (responseType == INVALID_USERNAME)
        {
            error = CreateBufferForResponse(Command, "Error: Invalid username / password combination\n", Client->Client);
            return error;
        }
      
        else if (responseType == USER_ALREADY_LOGGED_IN_ANOTHER_CONNECTION)
        {
            error = CreateBufferForResponse(Command, "Error: User already logged in\n", Client->Client);
            return error;
        }
        break;
    
    case CMD_LOGOUT:
        SynchronizeThread(NULL,Client, LOGOUT_UPDATE_LIST,NULL, NULL);
        error = CreateBufferForResponse(Command, "Success\n", Client->Client);
        return error;
    
    case CMD_LIST:
        connectedClientsList = (char*)calloc(BUFFER_LIST_CLIENTS, sizeof(char));
        SynchronizeThread(NULL, NULL, PRINT_CONNECTED_CLIENTS, &connectedClientsList, NULL);
        error = CreateBufferForResponse(CMD_LIST, connectedClientsList, Client->Client);
        return error;
    
    case CMD_MSG:
        username = (char*)malloc(((PMSGSTRUCT)Info)->UsernameLength * sizeof(char) + 1);
        memcpy(username, ((PMSGSTRUCT)Info)->FieldAccess, ((PMSGSTRUCT)Info)->UsernameLength);
        memset(username + ((PMSGSTRUCT)Info)->UsernameLength, 0, 1);
        SynchronizeFileHistory(username, Client, WRITE_FILE, 0, (PMSGSTRUCT)Info);
        return error;

    case CMD_BROADCAST:
        SynchronizeThread(DataForBroadcast, Client, BROADCAST_SEND_MESSAGE,NULL, NULL);
        break;

    case CMD_EXIT:
        SynchronizeThread(NULL, Client, EXIT_CLIENT, NULL, NULL);
        error = CreateBufferForResponse(Command, "Succes\n", Client->Client);
        break;
    
    case CMD_HISTORY:
        ParseDataForHistory((PHISTORY)Info, &username, &messageCounterForHistory);
        SynchronizeFileHistory(username, Client, READ_FILE, messageCounterForHistory,NULL);
        break;

    //cazuri folosite pentru a rezolva problema cu maximum de conexiuni concurente
    case MAXIMUM_CONNECTION:
        error = CreateBufferForResponse(Command,"Error: maximum concurrent connection count reached\n", Client->Client);
        return error;

    case CMD_UNKNOWN:
        error = CreateBufferForResponse(Command, "Nothing", Client->Client);
        return error;
    }
    return error;
}

void ListenToClientMessage(LPVOID Arg)
{
    CM_DATA_BUFFER* dataToReceive = NULL;
    PCLIENT_INFO data = (PCLIENT_INFO)Arg;
    CM_SIZE receivedByteCount = 0;
    PCLIENT_DATA info = NULL;
    PECHO echoBuffer = NULL;
    PREGISTER registerBuffer = NULL;
    PBROADCAST broadcastBuffer = NULL;
    PMSGSTRUCT msgBuffer = NULL;
    PHISTORY historyBuffer = NULL;
    CM_ERROR error = CM_SUCCESS;

    error = CreateDataBuffer(&dataToReceive, BUFFER_SIZE);

    if (CM_IS_ERROR(error))
    {
        printf("Unable to allocate buffer in client thread!\n");
        return;
    }

    while (TRUE)
    {
        error = ReceiveDataFromClient(data->Client, dataToReceive, &receivedByteCount);
        if (error == CM_CONNECTION_RECEIVE_FAILED)
        {
            SynchronizeThread(NULL,data, EXIT_CLIENT, NULL,NULL);
            AbandonClient(data->Client);
            break;
        }
        info = (PCLIENT_DATA)dataToReceive->DataBuffer;

        switch (info->Comand)
        {
        case CMD_ECHO:
            echoBuffer = (PECHO)info->Buffer;
            printf_s("%s\n", echoBuffer->FieldAccess);
            SendToClientForEchoCommand(data->Client, dataToReceive);
            break;

        case CMD_REGISTER:
            registerBuffer = (PREGISTER)info->Buffer;
            SendResponseToClient(CMD_REGISTER, registerBuffer, data,NULL);
            break;

        case CMD_LOGIN:
            registerBuffer = (PREGISTER)info->Buffer;
            SendResponseToClient(CMD_LOGIN, registerBuffer, data,NULL);
            break;

        case CMD_LOGOUT:
            SendResponseToClient(CMD_LOGOUT, NULL, data,NULL);
            break;
        
        case CMD_MSG:
            msgBuffer = (PMSGSTRUCT)info->Buffer;
            SendResponseToClient(CMD_MSG, msgBuffer, data, NULL);
            break;

        case CMD_BROADCAST:
            broadcastBuffer = (PBROADCAST)info->Buffer;
            SendResponseToClient(CMD_BROADCAST, NULL, data, (char*)broadcastBuffer->ServerResponse);
            break;

        case CMD_LIST:
            SendResponseToClient(CMD_LIST, NULL, data,NULL);
            break;

        case CMD_EXIT:
            SendResponseToClient(CMD_EXIT, NULL, data, NULL);
            break;

        case CMD_HISTORY:
            historyBuffer = (PHISTORY)info->Buffer;
            SendResponseToClient(CMD_HISTORY, (PHISTORY)historyBuffer, data, NULL);
            break;

        default:
            break;
        }
    }
    DestroyDataBuffer(dataToReceive);
    CloseHandle(GetCurrentThread());
}

void WaitForClients()
{
    CM_ERROR error = CM_SUCCESS;
	PCLIENT_INFO newClientInfo = NULL;
	newClientInfo = (PCLIENT_INFO)malloc(sizeof(CLIENT_INFO) + CLIENT_INFO_MAX_SIZE);

	while (TRUE) 
    {
		error = AwaitNewClient(server, &newClientInfo->Client);
        if (gNrConnections == gMaxNumberOfClients)
        {
            SendResponseToClient(MAXIMUM_CONNECTION,NULL,newClientInfo,NULL);
            continue;
        }

        SendResponseToClient(CMD_UNKNOWN, NULL, newClientInfo,NULL);

		if (CM_IS_ERROR(error))
		{
			_tprintf_s(TEXT("AwaitNewClient failed with err-code=0x%X!\n"), error);
			DestroyServer(server);
			UninitCommunicationModule();
			continue;
		}

		HANDLE hThread = NULL;
		hThread = CreateThread(
			NULL,
			0,
			(LPTHREAD_START_ROUTINE)ListenToClientMessage,
            newClientInfo,
			0,
			NULL
		);
		if (hThread != NULL)
		{
            SynchronizeThread(NULL, newClientInfo, ADD_LIST, NULL,NULL);
		}
        else
        {
            CloseHandle(hThread);
            return;
        }
        newClientInfo = (PCLIENT_INFO)malloc(sizeof(CLIENT_INFO) + CLIENT_INFO_MAX_SIZE);
    }
		
        //WaitForMultipleObjects(gNrConnections, gThreadPool, TRUE, INFINITE);
}
	


int _tmain(int argc, TCHAR* argv[])
{

    TCHAR* stopString;

    if (argc != 2)
    {
        _tprintf_s(TEXT("Error:Invalid maximum number of connections\n"));
        return -1;
    }
    else
    {
        gMaxNumberOfClients = (DWORD)wcstod(argv[1], &stopString);
        if (gMaxNumberOfClients == 0 || wcscmp(stopString, L""))
        {
            _tprintf_s(TEXT("Error:Invalid maximum number of connections\n"));

            return -1;
        }
        _tprintf_s(TEXT("Succes\n"));
    }

    EnableCommunicationModuleLogger();

    CM_ERROR error = InitCommunicationModule();
    if (CM_IS_ERROR(error))
    {
        _tprintf_s(TEXT("InitCommunicationModule failed with err-code=0x%X!\n"), error);
        return -1;
    }

    error = CreateServer(&server);
    if (CM_IS_ERROR(error))
    {
        _tprintf_s(TEXT("CreateServer failed with err-code=0x%X!\n"), error);
        UninitCommunicationModule();
        return -1;
    }

    ghMutex = CreateMutex(NULL,
        FALSE,
        NULL);

    if (ghMutex == NULL)
    {
        _tprintf_s(TEXT("CreateMutex error: %d\n"), GetLastError());
        return -1;
    }

    ghMutexFile = CreateMutex(NULL,
        FALSE,
        NULL);

    if (ghMutexFile == NULL)
    {
        _tprintf_s(TEXT("CreateMutex error: %d\n"), GetLastError());
        return -1;
    }

    ghMutexFileHistory = CreateMutex(NULL,
        FALSE,
        NULL);

    if (ghMutexFileHistory == NULL)
    {
        _tprintf_s(TEXT("CreateMutex error: %d\n"), GetLastError());
        return -1;
    }

    ghMutexFileOfflineMessage = CreateMutex(NULL,
        FALSE,
        NULL);

    if (ghMutexFileOfflineMessage == NULL)
    {
        _tprintf_s(TEXT("CreateMutex error: %d\n"), GetLastError());
        return -1;
    }


    //alocare threadpool
    gThreadPool = (HANDLE*)malloc(gMaxNumberOfClients * sizeof(HANDLE));

    HANDLE hThreadWaitForClient = NULL;
    hThreadWaitForClient = CreateThread(
        NULL,
        0,
        (LPTHREAD_START_ROUTINE)WaitForClients,
        NULL,
        0,
        NULL
    );

    if (hThreadWaitForClient == NULL)
    {
        _tprintf_s(TEXT("Thread cannot be created"));
        CloseHandle(hThreadWaitForClient);
        UninitCommunicationModule();
        return -1;
    }

    HANDLE hCreateFile, hMapFile;
    hCreateFile = CreateFileA(
          FILE_PATH
        , GENERIC_READ | GENERIC_WRITE
        , 0
        , NULL 
        , OPEN_EXISTING
        , FILE_ATTRIBUTE_NORMAL
        , NULL);

    if (hCreateFile == INVALID_HANDLE_VALUE)
    {
        hCreateFile = CreateFileA(
            FILE_PATH
            , GENERIC_READ | GENERIC_WRITE
            , 0
            , NULL
            , CREATE_ALWAYS
            , FILE_ATTRIBUTE_NORMAL
            , NULL);
        if (hCreateFile == INVALID_HANDLE_VALUE)
        {
            printf("Error %x", GetLastError());
            return -1;
        }
    }

    hMapFile = CreateFileMappingA(
        hCreateFile,    
        NULL,                    
        PAGE_READWRITE,          
        0,                       
        FILE_SIZE,
        NULL);             
    if (hMapFile == NULL)
    {
         _tprintf(TEXT("Could not create file mapping object (%d).\n"),
             GetLastError());
        return 1;
    }
    
    gFileStart = (PBYTE)MapViewOfFile(hMapFile,
        FILE_MAP_ALL_ACCESS,
        0,
        0,
        GetFileSize(hCreateFile, NULL));
    gFileEnd = gFileStart;
    
    while ((*gFileEnd) != 0 && gFileEnd -gFileStart<=FILE_SIZE)
    {
        gFileEnd++;
    }

    //crearea fisierului History

    HANDLE hCreateHystoryFile, hMapHystoryFile;
    hCreateHystoryFile = CreateFileA(
          FILE_HISTORY_PATH
        , GENERIC_READ | GENERIC_WRITE
        , 0
        , NULL
        , OPEN_EXISTING
        , FILE_ATTRIBUTE_NORMAL
        , NULL);

    if (hCreateHystoryFile == INVALID_HANDLE_VALUE)
    {
        hCreateHystoryFile = CreateFileA(
              FILE_HISTORY_PATH
            , GENERIC_READ | GENERIC_WRITE
            , 0
            , NULL
            , CREATE_ALWAYS
            , FILE_ATTRIBUTE_NORMAL
            , NULL);
        if (hCreateHystoryFile == INVALID_HANDLE_VALUE)
        {
            printf("Error %x", GetLastError());
            return -1;
        }
    }

    hMapHystoryFile = CreateFileMappingA(
        hCreateHystoryFile,
        NULL,
        PAGE_READWRITE,
        0,
        FILE_SIZE,
        NULL);

    if (hMapHystoryFile == NULL)
    {
        _tprintf(TEXT("Could not create file mapping object (%d).\n"),
            GetLastError());
        return -1;
    }

    gFileHistoryStart = (PBYTE)MapViewOfFile(hMapHystoryFile,
        FILE_MAP_ALL_ACCESS,
        0,
        0,
        GetFileSize(hCreateHystoryFile, NULL));
    
    gFileHistoryEnd = gFileHistoryStart;
    while ((*gFileHistoryEnd) != 0 && gFileHistoryEnd - gFileHistoryStart <= FILE_SIZE)
    {
       gFileHistoryEnd++;
    }

    //crearea fisierului pentru primirea mesajelor offline
    HANDLE hCreateOfflineMessageFile, hMapOfflineMessageFile;
    hCreateOfflineMessageFile = CreateFileA(
        FILE_OFFLINE_MESSAGE_PATH
        , GENERIC_READ | GENERIC_WRITE
        , 0
        , NULL
        , OPEN_EXISTING
        , FILE_ATTRIBUTE_NORMAL
        , NULL);

    if (hCreateOfflineMessageFile == INVALID_HANDLE_VALUE)
    {
        hCreateOfflineMessageFile = CreateFileA(
            FILE_OFFLINE_MESSAGE_PATH
            , GENERIC_READ | GENERIC_WRITE
            , 0
            , NULL
            , CREATE_ALWAYS
            , FILE_ATTRIBUTE_NORMAL
            , NULL);
        if (hCreateOfflineMessageFile == INVALID_HANDLE_VALUE)
        {
            printf("Error %x", GetLastError());
            return -1;
        }
    }

    hMapOfflineMessageFile = CreateFileMappingA(
        hCreateOfflineMessageFile,
        NULL,
        PAGE_READWRITE,
        0,
        FILE_SIZE,
        NULL);

    if (hMapOfflineMessageFile == NULL)
    {
        _tprintf(TEXT("Could not create file mapping object (%d).\n"),
            GetLastError());
        return -1;
    }

    gFileOfflineMessageStart = (PBYTE)MapViewOfFile(hMapOfflineMessageFile,
        FILE_MAP_ALL_ACCESS,
        0,
        0,
        GetFileSize(hCreateOfflineMessageFile, NULL));

    gFileOfflineMessageEnd = gFileOfflineMessageStart;

    while ((*gFileOfflineMessageEnd) != 0 && gFileOfflineMessageEnd - gFileOfflineMessageStart <= FILE_SIZE)
    {
        gFileOfflineMessageEnd++;
    }

    WaitForSingleObject(hThreadWaitForClient,INFINITE);
    _tprintf_s(TEXT("Server is shutting down now...\n"));

    UnmapViewOfFile(gFileOfflineMessageStart);
    CloseHandle(hMapOfflineMessageFile);
    CloseHandle(hCreateOfflineMessageFile);

    UnmapViewOfFile(gFileHistoryStart);
    CloseHandle(hMapHystoryFile);
    CloseHandle(hCreateHystoryFile);

    UnmapViewOfFile(gFileStart);
    CloseHandle(hMapFile);
    CloseHandle(hCreateFile);

    DestroyServer(server);
    UninitCommunicationModule();
    return 0;
 }
