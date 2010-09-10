#include <windows.h>
#include <stdio.h>
#include <stdlib.h>

/**
 * Para executar o programa é necessário passar como argumento um inteiro especificando
 * a quantidade de elementos da sequência de Fibonacci a serem impressos.
 */
int main(int argc, char** argv) {
	if (argc != 2) {
		printf("%s", "Número de parametros inválido!");
		return -1;
	}
	STARTUPINFO si;
	PROCESS_INFORMATION pi;
	ZeroMemory( &si, sizeof(si) );
	si.cb = sizeof(si);
	ZeroMemory( &pi, sizeof(pi) );

	char* programa = "Fibonacci.exe ";
	char* command = (char*) calloc(strlen(programa) + strlen(argv[1]), sizeof(char));
	strcat(command, programa);
	strcat(command, argv[1]);

	// Start the child process.
	if (!CreateProcess(NULL, // No module name (use command line).
			command, // Command line.
			NULL, // Process handle not inheritable.
			NULL, // Thread handle not inheritable.
			FALSE, // Set handle inheritance to FALSE.
			0, // No creation flags.
			NULL, // Use parent's environment block.
			NULL, // Use parent's starting directory.
			&si, // Pointer to STARTUPINFO structure.
			&pi) // Pointer to PROCESS_INFORMATION structure.
		) {
		printf("A criação do processo falhou. Erro No.: (%d).\n", (int) GetLastError());
		return -1;
	}

	// Wait until child process exits.
	WaitForSingleObject(pi.hProcess, INFINITE);

	// Close process and thread handles.
	CloseHandle(pi.hProcess);
	CloseHandle(pi.hThread);
	return 0;
}

