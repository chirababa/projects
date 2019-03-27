// MessageClient.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"

// communication library
#include "communication_api.h"
#include "common_structure.h"
#include <windows.h>
#include <stdio.h>
#include <stdlib.h>
#include<string.h>

BOOLEAN gUserAlreadyLoggedIn = FALSE;
HANDLE ghThreadClient = NULL;
BOOLEAN gExitFlag = FALSE;

CM_ERROR SendEchoCommand(const char* Info, CM_CLIENT* Client){
    PCLIENT_DATA clientData = NULL;
    CM_ERROR error = CM_SUCCESS;
    PECHO pEchoData = NULL;
    CM_DATA_BUFFER* dataToSend = NULL;
    CM_SIZE sendBytesCount = 0;
    CM_SIZE bufferSize = sizeof(CLIENT_DATA) + sizeof(ECHO) + BUFFER_SIZE;
    clientData = (PCLIENT_DATA)malloc(bufferSize);

    if (clientData == NULL)
    {
        return CM_NO_MEMORY;
    }

    clientData->Comand = CMD_ECHO;
    pEchoData = (PECHO)clientData->Buffer;
    memcpy(pEchoData->FieldAccess, (PBYTE)Info, strlen(Info) * sizeof(char) + 1);
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
    error = SendDataToServer(Client,dataToSend, &sendBytesCount);
    if (CM_IS_ERROR(error))
    {
        goto cleanup;
    }
    
cleanup:
    if (clientData != NULL)
    {
        DestroyDataBuffer(dataToSend);
        free(clientData);
    }
    return error;
}

CM_ERROR SendRegisterCommandInfo(const char* Info, CM_CLIENT* Client, COMMAND_TYPE CommandType) 
{
    PCLIENT_DATA clientData = NULL;
    CM_ERROR error = CM_SUCCESS;
    PREGISTER pRegisterData = NULL;
    CM_DATA_BUFFER* dataToSend = NULL;
    CM_SIZE sendBytesCount = 0;
    CM_SIZE bufferSize = sizeof(CLIENT_DATA) + sizeof(REGISTER) + BUFFER_SIZE;
    clientData = (PCLIENT_DATA)malloc(bufferSize);

    char* infoCopy = (char *)malloc(strlen(Info) * sizeof(char)+1);
    memcpy(infoCopy, Info, strlen(Info) * sizeof(char)+1);
    char*pch = strtok(infoCopy, " ");
    int dataLenght[2];
    int i = 0;//variabila prin care indexez vectorul de lungimi ale usernameului si a parolei
    
    while (pch != NULL)
    {
        if (i == 2)
        {
            _tprintf_s(TEXT("Error: Invalid password\n"));
            error = CM_INVALID_PARAMETER;
            return error;
        }
        dataLenght[i++] = (int)strlen(pch);
        pch = strtok(NULL, " ");
    }

    if (i==0|| i==1)
    {
        _tprintf_s(TEXT("Error: Invalid PARAMETER\n"));
        error = CM_INVALID_PARAMETER;
        return error;
    }
  
    if (clientData == NULL)
    {
        return CM_NO_MEMORY;
    }
    if (CommandType == CMD_REGISTER)
    {
        clientData->Comand = CMD_REGISTER;
    }
    else if (CommandType == CMD_LOGIN)
    {
        clientData->Comand = CMD_LOGIN;
    }
    pRegisterData = (PREGISTER)clientData->Buffer;
    pRegisterData->UsernameLength = dataLenght[0];
    pRegisterData->PasswordLength = dataLenght[1];
    memcpy(pRegisterData->FieldAccess, (PBYTE)Info, strlen(Info) * sizeof(char) + 1);
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
    error = SendDataToServer(Client, dataToSend, &sendBytesCount);
    if (CM_IS_ERROR(error))
    {
        goto cleanup;
    }

cleanup:
    if (clientData != NULL)
    {
        DestroyDataBuffer(dataToSend);
        free(clientData);
    }
    return error;
}

CM_ERROR SendMsgDataToServer(const char* Info, CM_CLIENT* Client)
{
    PCLIENT_DATA clientData = NULL;
    CM_ERROR error = CM_SUCCESS;
    PMSGSTRUCT pMsgData = NULL;
    CM_DATA_BUFFER* dataToSend = NULL;
    CM_SIZE sendBytesCount = 0;
    CM_SIZE bufferSize = sizeof(CLIENT_DATA) + sizeof(REGISTER) + BUFFER_SIZE;
    clientData = (PCLIENT_DATA)malloc(bufferSize);

    if (clientData == NULL)
    {
        return CM_NO_MEMORY;
    }

    char* infoCopy = (char *)malloc(strlen(Info) * sizeof(char) + 1);
    memcpy(infoCopy, Info, strlen(Info) * sizeof(char) + 1);
    char*pch = strtok(infoCopy, " ");

    if (pch == NULL)
    {
        _tprintf_s(TEXT("Error: Invalid parameters\n"));
        error = CM_INVALID_PARAMETER;
        return error;
    }

    clientData->Comand = CMD_MSG;
    pMsgData = (PMSGSTRUCT)clientData->Buffer;
    pMsgData->UsernameLength = (DWORD)strlen(pch);

    pMsgData->MessageLength = (DWORD)strlen(Info) - (DWORD)strlen(pch) - 1;

    memcpy(pMsgData->FieldAccess, (PBYTE)Info, strlen(Info) * sizeof(char) + 1);

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
    error = SendDataToServer(Client, dataToSend, &sendBytesCount);
    if (CM_IS_ERROR(error))
    {
        goto cleanup;
    }

cleanup:
    if (clientData != NULL)
    {
        DestroyDataBuffer(dataToSend);
        free(clientData);
    }
    return error;
}

CM_ERROR SendHistoryData(const char* Info, CM_CLIENT* Client)
{
    PCLIENT_DATA clientData = NULL;
    CM_ERROR error = CM_SUCCESS;
    PHISTORY pHistoryData = NULL;
    CM_DATA_BUFFER* dataToSend = NULL;
    CM_SIZE sendBytesCount = 0;
    CM_SIZE bufferSize = sizeof(CLIENT_DATA) + sizeof(HISTORY) + BUFFER_SIZE;
    clientData = (PCLIENT_DATA)malloc(bufferSize);
    
    if (clientData == NULL)
    {
        return CM_NO_MEMORY;
    }

    char* infoCopy = (char *)malloc(strlen(Info) * sizeof(char) + 1);
    memcpy(infoCopy, Info, strlen(Info) * sizeof(char) + 1);
    char*pch = strtok(infoCopy, " ");

    if (pch == NULL)
    {
        _tprintf_s(TEXT("Error: Invalid parameters\n"));
        error = CM_INVALID_PARAMETER;
        return error;
    }

    clientData->Comand = CMD_HISTORY;
    pHistoryData = (PHISTORY)clientData->Buffer;
    pHistoryData->UsernameLength = (int)strlen(pch);
    pch = strtok(NULL, " ");

    if (pch == NULL)
    {
        _tprintf_s(TEXT("Error: Invalid parameters\n"));
        error = CM_INVALID_PARAMETER;
        return error;
    }

    for (int i = 0; i < strlen(pch); i++)
    {
        if (!isdigit(pch[i]))
        {
            _tprintf_s(TEXT("Error: Invalid parameters\n"));
            error = CM_INVALID_PARAMETER;
            return error;
        }
    }

    pHistoryData->MessageCounter = atoi(pch);
    memcpy(pHistoryData->FieldAccess, (PBYTE)Info, strlen(Info) * sizeof(char) + 1);
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
    error = SendDataToServer(Client, dataToSend, &sendBytesCount);
    if (CM_IS_ERROR(error))
    {
        goto cleanup;
    }

cleanup:
    if (clientData != NULL)
    {
        DestroyDataBuffer(dataToSend);
        free(clientData);
    }
    return error;
}

CM_ERROR SendLogoutData(const char* Info, CM_CLIENT* Client)
{
    PCLIENT_DATA clientData = NULL;
    CM_ERROR error = CM_SUCCESS;
    PLOGOUT pLogoutData = NULL;
    CM_DATA_BUFFER* dataToSend = NULL;
    CM_SIZE sendBytesCount = 0;
    CM_SIZE bufferSize = sizeof(CLIENT_DATA) + sizeof(LOGOUT) + 100;
    clientData = (PCLIENT_DATA)malloc(bufferSize);

    if (clientData == NULL)
    {
        return CM_NO_MEMORY;
    }

    clientData->Comand = CMD_LOGOUT;
    pLogoutData = (PLOGOUT)clientData->Buffer;
    memcpy(pLogoutData->FieldAccess, (PBYTE)Info, strlen(Info) * sizeof(char) + 1);
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
    error = SendDataToServer(Client, dataToSend, &sendBytesCount);
    if (CM_IS_ERROR(error))
    {
        goto cleanup;
    }

cleanup:
    if (clientData != NULL)
    {
        DestroyDataBuffer(dataToSend);
        free(clientData);
    }
    return error;
}

CM_ERROR SendBroadcastData(const char* Info, CM_CLIENT* Client)
{
    PCLIENT_DATA clientData = NULL;
    CM_ERROR error = CM_SUCCESS;
    PBROADCAST pBroadcastData = NULL;
    CM_DATA_BUFFER* dataToSend = NULL;
    CM_SIZE sendBytesCount = 0;
    CM_SIZE bufferSize = sizeof(CLIENT_DATA) + sizeof(BROADCAST) + BUFFER_SIZE;
    clientData = (PCLIENT_DATA)malloc(bufferSize);

    if (clientData == NULL)
    {
        return CM_NO_MEMORY;
    }

    clientData->Comand = CMD_BROADCAST;
    pBroadcastData = (PBROADCAST)clientData->Buffer;
    memcpy(pBroadcastData->ServerResponse, (PBYTE)Info, strlen(Info) * sizeof(char) + 1);
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
    error = SendDataToServer(Client, dataToSend, &sendBytesCount);
    if (CM_IS_ERROR(error))
    {
        goto cleanup;
    }

cleanup:
    if (clientData != NULL)
    {
        DestroyDataBuffer(dataToSend);
        free(clientData);
    }
    return error;
}

CM_ERROR SendListData(const char* Info, CM_CLIENT* Client)
{
    PCLIENT_DATA clientData = NULL;
    CM_ERROR error = CM_SUCCESS;
    PLIST pListData = NULL;
    CM_DATA_BUFFER* dataToSend = NULL;
    CM_SIZE sendBytesCount = 0;
    CM_SIZE bufferSize = sizeof(CLIENT_DATA) + sizeof(PLIST) + 100;
    clientData = (PCLIENT_DATA)malloc(bufferSize);

    if (clientData == NULL)
    {
        return CM_NO_MEMORY;
    }

    clientData->Comand = CMD_LIST;
    pListData = (PLIST)clientData->Buffer;
    memcpy(pListData->FieldAccess, (PBYTE)Info, strlen(Info) * sizeof(char) + 1);
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
    error = SendDataToServer(Client, dataToSend, &sendBytesCount);
    if (CM_IS_ERROR(error))
    {
        goto cleanup;
    }

cleanup:
    if (clientData != NULL)
    {
        DestroyDataBuffer(dataToSend);
        free(clientData);
    }
    return error;
}

CM_ERROR SendExitData(const char* Info, CM_CLIENT* Client)
{
    PCLIENT_DATA clientData = NULL;
    CM_ERROR error = CM_SUCCESS;
    PEXIT pExistData = NULL;
    CM_DATA_BUFFER* dataToSend = NULL;
    CM_SIZE sendBytesCount = 0;
    CM_SIZE bufferSize = sizeof(CLIENT_DATA) + sizeof(PLIST) + 100;
    clientData = (PCLIENT_DATA)malloc(bufferSize);

    if (clientData == NULL)
    {
        return CM_NO_MEMORY;
    }

    clientData->Comand = CMD_EXIT;
    pExistData = (PLIST)clientData->Buffer;
    memcpy(pExistData->FieldAccess, (PBYTE)Info, strlen(Info) * sizeof(char) + 1);
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
    error = SendDataToServer(Client, dataToSend, &sendBytesCount);
    if (CM_IS_ERROR(error))
    {
        goto cleanup;
    }

cleanup:
    if (clientData != NULL)
    {
        DestroyDataBuffer(dataToSend);
        free(clientData);
    }
    return error;
}

void ReceiveFromServerEcho(PECHO Info) {
    printf_s("%s\n", Info->FieldAccess);
}

void ReceiveFromServerList(PLIST Info) {
    printf_s("%s\n", Info->FieldAccess);
}

void ReceiveInfoFromServer(PRESPONSE_FROM_SERVER Info) {
    printf_s("%s", Info->ServerResponse);
}

void ReceiveFromServerBroadcast(PBROADCAST Info) {
    printf_s("%s", Info->ServerResponse);
}

void ReceiveDataFromServer(LPVOID Arg)
{
    //BOOLEAN readyToExit = FALSE;
    CM_ERROR error = CM_SUCCESS;
    CM_DATA_BUFFER* dataToReceive = NULL;
    CM_SIZE receivedByteCount = 0;
    PCLIENT_DATA info = NULL;
    error = CreateDataBuffer(&dataToReceive, BUFFER_SIZE);
    while (!gExitFlag)
    {
        error = ReceiveDataFormServer(Arg, dataToReceive, &receivedByteCount);
        info = (PCLIENT_DATA)dataToReceive->DataBuffer;

        switch (info->Comand)
        {
        case CMD_ECHO:
            ReceiveFromServerEcho((PECHO)info->Buffer);
            break;

        case CMD_REGISTER:
            ReceiveInfoFromServer((PRESPONSE_FROM_SERVER)info->Buffer);
            break;

        case CMD_LOGIN:
            ReceiveInfoFromServer((PRESPONSE_FROM_SERVER)info->Buffer);
            if (strcmp(((char*)(PRESPONSE_FROM_SERVER)info->Buffer), "Success\n") == 0)
            {
                gUserAlreadyLoggedIn = TRUE;
            }
            break;

        case CMD_LOGOUT:
            ReceiveInfoFromServer((PRESPONSE_FROM_SERVER)info->Buffer);
            break;

        case CMD_MSG:
            ReceiveInfoFromServer((PRESPONSE_FROM_SERVER)info->Buffer);
            break;
        
        case CMD_LIST:
            ReceiveFromServerList((PLIST)info->Buffer);
            break;

        case CMD_BROADCAST:
            ReceiveFromServerBroadcast((PBROADCAST)info->Buffer);
            break;

        case CMD_EXIT:
            gExitFlag = TRUE;
            break;

        case CMD_HISTORY:
            ReceiveFromServerBroadcast((PRESPONSE_FROM_SERVER)info->Buffer);
            break;

        case MAXIMUM_CONNECTION:
            ReceiveInfoFromServer((PRESPONSE_FROM_SERVER)info->Buffer);
            break;

        case CMD_UNKNOWN:
            break;
        }
    }
    DestroyDataBuffer(dataToReceive);
}

COMMAND_TYPE GetCommand(const char* input)
{
    if (strstr(input, "echo") != NULL && strstr(input, "echo") == input && strncmp(strstr(input, "echo"),"echo",4)==0)
    {
        return CMD_ECHO;
    }
    else if (strstr(input, "register") != NULL && strstr(input, "register") == input)
    {
        return CMD_REGISTER;
    }
    else if (strstr(input,"login") != NULL && strstr(input, "login") == input)
    {
        return CMD_LOGIN;
    }
    else if (strstr(input, "logout") != NULL && strstr(input, "logout") == input)
    {
        return CMD_LOGOUT;
    }
    else if (strstr(input, "msg") != NULL && strstr(input, "msg") == input)
    {
        return CMD_MSG;
    }
    else if (strstr(input, "broadcast") != NULL && strstr(input, "broadcast") == input)
    {
        return CMD_BROADCAST;
    }
    else if (strstr(input, "sendfile") != NULL && strstr(input, "sendfile") == input)
    {
        return CMD_SENDFILE;
    }
    else if (strstr(input, "list") != NULL && strstr(input, "list") == input)
    {
        return CMD_LIST;
    }
    else if (strstr(input, "exit") != NULL && strstr(input, "exit") == input)
    {
        return CMD_EXIT;
    }
    else if (strstr(input, "history") != NULL && strstr(input, "history") == input)
    {
        return CMD_HISTORY;
    }

     return CMD_UNKNOWN;
}

BOOLEAN IsEmptyCommand(COMMAND_TYPE Command, const char* info)
{
    switch (Command)
    {
    case CMD_ECHO:
        if (strlen(info) == 4)
        {
            _tprintf_s(TEXT("Empty message.Try again\n"));
            return FALSE;
        }
        break;

    case CMD_REGISTER:
        if (strlen(info) == 8)
        {
            _tprintf_s(TEXT("Error: Invalid username\n"));
            return FALSE;
        }
        break;

    case CMD_LOGIN:
        if (strlen(info) == 5)
        {
            _tprintf_s(TEXT("Error: Invalid username/password combination\n"));
            return FALSE;
        }
        break;

    case CMD_MSG:
        if (strlen(info) == 3)
        {
            _tprintf_s(TEXT("Empty message.Try again\n"));
            return FALSE;
        }
        break;

    case CMD_BROADCAST:
        if (strlen(info) == 9)
        {
            _tprintf_s(TEXT("Empty message.Try again\n"));
            return FALSE;
        }
        break;

    case CMD_HISTORY:
        if (strlen(info) == 7)
        {
            _tprintf_s(TEXT("Invalid Parameters"));
            return FALSE;
        }
        break;
    }

    return TRUE;
}
   
int _tmain(int argc, TCHAR* argv[])
{
    /*
        This main implementation can be used as an initial example.
        You can erase main implementation when is no longer helpful.
    */
    
    (void)argc;
    (void)argv;
    
    CM_DATA_BUFFER* dataToReceive = NULL;
    CM_SIZE receivedByteCount = 0;
    PCLIENT_DATA ClientData = NULL;

    CM_ERROR error = InitCommunicationModule();
    if (CM_IS_ERROR(error))
    {
        _tprintf_s(TEXT("InitCommunicationModule failed with err-code=0x%X!\n"), error);
        return -1;
    }

    CM_CLIENT* client = NULL;
    error = CreateClientConnectionToServer(&client);
    if (CM_IS_ERROR(error))
    {
        //_tprintf_s(TEXT("CreateClientConnectionToServer failed with err-code=0x%X!\n"), error);
        _tprintf_s(TEXT("Error: no running server found\n"));
        UninitCommunicationModule();
        return -1;
    }

    error = CreateDataBuffer(&dataToReceive, BUFFER_SIZE);
    if (CM_IS_ERROR(error))
    {
        _tprintf_s(TEXT("Failed to create RECEIVE data buffer with err-code=0x%X!\n"), error);
        DestroyClient(client);
        UninitCommunicationModule();
        return -1;
    }

    error = ReceiveDataFormServer(client, dataToReceive, &receivedByteCount);
    if (CM_IS_ERROR(error))
    {
        _tprintf_s(TEXT("ReceiveDataFormServer failed with err-code=0x%X!\n"), error);
        DestroyDataBuffer(dataToReceive);
        DestroyClient(client);
        UninitCommunicationModule();
        return -1;
    }

    ClientData = (PCLIENT_DATA)dataToReceive->DataBuffer;
    PRESPONSE_FROM_SERVER data = (PRESPONSE_FROM_SERVER)ClientData->Buffer;

    if (strcmp((char*)data->ServerResponse, "Error: maximum concurrent connection count reached\n") == 0)
    {
        _tprintf_s(TEXT("Error: maximum concurrent connection count reached\n"));
        DestroyDataBuffer(dataToReceive);
        DestroyClient(client);
        UninitCommunicationModule();
        return -1;
    }
    _tprintf_s(TEXT("Successful connection\n"));

    ghThreadClient = CreateThread(
        NULL,
        0,
        (LPTHREAD_START_ROUTINE)ReceiveDataFromServer,
        client,
        0,
        NULL
    );

    if (ghThreadClient == NULL)
    {
        _tprintf_s(TEXT("Thread cannot be created"));
        CloseHandle(ghThreadClient);
        DestroyDataBuffer(dataToReceive);
        DestroyClient(client);
        UninitCommunicationModule();
        return -1;
    }
  
    char info[BUFFER_SIZE];
    while (!gExitFlag)
    {
        gets_s(info, BUFFER_SIZE);
        COMMAND_TYPE command = GetCommand(info);
        EnableCommunicationModuleLogger();
        switch (command)
        {
        case CMD_ECHO:
            if (IsEmptyCommand(command, info))
            {
                SendEchoCommand(info + 5, client);
            }
            break;

        case CMD_REGISTER:
            if (gUserAlreadyLoggedIn == TRUE)
            {
                _tprintf_s(TEXT("Error: User already logged in\n"));
                break;
            }

            if (IsEmptyCommand(command, info))
            {
                SendRegisterCommandInfo(info + 9, client, CMD_REGISTER);
            }
            break;

        case CMD_LOGIN:
            
            if (gUserAlreadyLoggedIn == TRUE)
            {
                _tprintf_s(TEXT("Error: Another user already logged in\n"));
                break;
            }

            if (IsEmptyCommand(command, info))
            {
                SendRegisterCommandInfo(info + 6, client, CMD_LOGIN);
            }
            break;

        case CMD_LOGOUT:
            if (gUserAlreadyLoggedIn == TRUE)
            {
                SendLogoutData(info, client);
                gUserAlreadyLoggedIn = FALSE;
            }
            else
            {
                _tprintf_s(TEXT("Error: No user currently logged in\n"));
            }
            break;

        case CMD_BROADCAST:
            if (gUserAlreadyLoggedIn == TRUE)
            {
                if (IsEmptyCommand(CMD_BROADCAST, info))
                {
                    SendBroadcastData(info + 10, client);
                }
            }
            else
            {
                _tprintf_s(TEXT("Error: No user currently logged in\n"));
            }
            break;

        case CMD_MSG:
            if (gUserAlreadyLoggedIn == TRUE)
            {
                if (IsEmptyCommand(CMD_MSG, info))
                {
                    SendMsgDataToServer(info + 4, client);
                }
            }
            else
            {
                _tprintf_s(TEXT("Error: No user currently logged in\n"));
            }
            break;

        case CMD_LIST:
            SendListData(info, client);
            break;

        case CMD_EXIT:
            SendExitData(info, client);
            WaitForSingleObject(ghThreadClient,INFINITE);
            break;

        case CMD_HISTORY:
            if (gUserAlreadyLoggedIn == TRUE)
            {
                SendHistoryData(info + 8, client);
            }
            else
            {
                _tprintf_s(TEXT("Error: No user currently logged in\n"));
            }
            break;

        case CMD_UNKNOWN:
            _tprintf_s(TEXT("Invalid command! Please try again\n"));
            break;

        default:
            break;
        }
    }
    CloseHandle(ghThreadClient);
    DestroyClient(client);
    UninitCommunicationModule();

    return 0;
}

