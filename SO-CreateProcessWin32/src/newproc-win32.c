#include <windows.h>
#include <stdio.h>
#include <stdlib.h>

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

	char* command = (char*) calloc(1000, sizeof(char));
	char* programa = "E:\\eclipse-cpp-galileo-win32\\ws\\Fibonacci\\Release\\Fibonacci.exe ";
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
		printf("CreateProcess failed (%d).\n", GetLastError());
		return -1;
	}

	// Wait until child process exits.
	WaitForSingleObject(pi.hProcess, INFINITE);

	// Close process and thread handles.
	CloseHandle(pi.hProcess);
	CloseHandle(pi.hThread);
	return 0;
}

