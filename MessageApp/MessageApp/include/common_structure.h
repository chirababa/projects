#ifndef _COMMON_STRUCTURE_H_
#define _COMMON_STRUCTURE_H_
#include <Windows.h>

#define BUFFER_SIZE 4096

#define FILE_SIZE 8192
#define MIN(a,b) (((a)<(b))? (a) : (b))

typedef enum _SYNC_OPERATION
{
    ADD_LIST,
    REMOVE_LIST,
    ADD_THREADPOOL,
    REMOVE_THREADPOOL,
    UPDATE_LOGGED_IN_CLIENTS,
    EXIT_CLIENT,
    WRITE_FILE,
    READ_FILE,
    VERIFY_REGISTERED_CLIENTS,
    LOGOUT_UPDATE_LIST,
    BROADCAST_SEND_MESSAGE,
    PRINT_CONNECTED_CLIENTS,
    MSG_SYNC,
    USER_LOGGED_IN //verificare daca clietul este logat alte conexiuni
}SYNC_OPERATION;

typedef enum _RESPONSE_SERVER_TYPE
{
	RESPONSE_SUCCES,
	INVALID_USERNAME,
	INVALID_PASSWORD,
	USERNAME_ALREADY_REGISTERED,
	PASSWORD_TOO_WEAK,
	USER_ALREADY_LOGGED_IN_ANOTHER_CONNECTION
}RESPONSE_SERVER_TYPE;

typedef enum _COMMAND_TYPE
{
	CMD_ECHO,
	CMD_REGISTER,
	CMD_LOGIN,
	CMD_LOGOUT,
	CMD_MSG,
	CMD_BROADCAST,
	CMD_SENDFILE,
	CMD_LIST,
	CMD_EXIT,
	CMD_HISTORY,
    MAXIMUM_CONNECTION,
	CMD_UNKNOWN
}COMMAND_TYPE;

typedef struct _CLIENT_DATA
{
	COMMAND_TYPE Comand;
#pragma warning (suppress:4200)
	BYTE Buffer[0];
}CLIENT_DATA, *PCLIENT_DATA;

typedef struct _ECHO
{
#pragma warning (suppress:4200)
	BYTE FieldAccess[0];
}ECHO, *PECHO;

typedef struct _REGISTER
{
	DWORD UsernameLength;
	DWORD PasswordLength;
#pragma warning (suppress:4200)
	BYTE FieldAccess[0];
}REGISTER, *PREGISTER, LOGIN, *PLOGIN;

typedef struct _LOGOUT
{
#pragma warning (suppress:4200)
    BYTE FieldAccess[0];
}LOGOUT, *PLOGOUT, LIST, *PLIST, EXIT,*PEXIT;

typedef struct _MSGSTRUCT
{
    DWORD UsernameLength;
    DWORD MessageLength;
#pragma warning (suppress:4200)
    BYTE FieldAccess[0];
}MSGSTRUCT,*PMSGSTRUCT;

typedef struct _HISTORY
{
    DWORD UsernameLength;
    DWORD MessageCounter;
#pragma warning (suppress:4200)
    BYTE FieldAccess[0];
}HISTORY, *PHISTORY;

typedef struct _RESPONSE_FROM_SERVER
{
#pragma warning (suppress:4200)
	BYTE ServerResponse[0];
}RESPONSE_FROM_SERVER, *PRESPONSE_FROM_SERVER, BROADCAST, *PBROADCAST;

#endif