
#include <stdio.h>
#include <stdlib.h>
#include <windows.h>

long gerarFibonacci(long n);
DWORD WINAPI geradorFibonacci(PVOID param);

long *fibonacciBuffer;
long tamanhoBuffer = 0;

int main(void) {
	long limite;
	puts("--- Gerador de números da sequência de Fibonacci ---");
	do {
		printf("Informe o limite da sequência: ");
		scanf("%ld", &limite);
		if (limite <= 0) {
			puts("O limite deve ser maior que 0.");
		}
	} while (limite <= 0);

	fibonacciBuffer = calloc(limite, sizeof(long));
	tamanhoBuffer = 0;

	DWORD threadId;
	HANDLE threadHandle = CreateThread(
			NULL, //A pointer to a SECURITY_ATTRIBUTES structure that determines whether the returned handle can be inherited by child processes. If lpThreadAttributes is NULL, the handle cannot be inherited.
			0, //The initial size of the stack, in bytes. The system rounds this value to the nearest page. If this parameter is zero, the new thread uses the default size for the executable.
			geradorFibonacci, //A pointer to the application-defined function to be executed by the thread.
			(PVOID) &limite, //A pointer to a variable to be passed to the thread.
			0, //The flags that control the creation of the thread.
			&threadId //A pointer to a variable that receives the thread identifier. If this parameter is NULL, the thread identifier is not returned.
	);

	if (threadHandle != NULL) {
		WaitForSingleObject(threadHandle, INFINITE);
		CloseHandle(threadHandle);
		long i;
		printf("Números da sequência: ");
		for (i = 0; i < limite; i++) {
			printf("%ld " + fibonacciBuffer[i]);
		}
	}

	return EXIT_SUCCESS;
}

long gerarFibonacci(long n) {
	//Se o n-ésimo número da série já estiver no buffer, retorna o valor do buffer:
	if (n < tamanhoBuffer) {
		return fibonacciBuffer[n];
	}
	long valor;
	if (n == 0) {
		valor = 0;
	} else if (n == 1) {
		valor = 1;
	} else {
		valor = gerarFibonacci(n - 1) + gerarFibonacci(n - 2);
	}
	fibonacciBuffer[tamanhoBuffer++] = valor;
	return valor;
}

DWORD WINAPI geradorFibonacci(PVOID param) {
	long limite = *(long*) param;
	long i;
	for (i = 0; i < limite; i++) {
		gerarFibonacci(i);
	}
	return 0;
}
