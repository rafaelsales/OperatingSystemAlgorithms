#include <stdio.h>
#include <stdlib.h>
#include <windows.h>

int gerarFibonacci(int n);
DWORD WINAPI geradorFibonacci(PVOID param);

int *fibonacciBuffer; //Array para armazenar elementos da serie de Fibonacci ja calculados
int tamanhoBuffer; //Quantidade de elementos da serie de Fibonacci ja calculados

int main(void) {
	int limite;
	puts("--- Gerador de numeros da sequencia de Fibonacci ---");
	do {
		printf("Informe o limite da sequencia: ");
		scanf("%d", &limite);
		if (limite <= 0) {
			puts("O limite deve ser maior que 0.");
		}
	} while (limite <= 0);

	fibonacciBuffer = (int *) calloc(limite, sizeof(int));
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

		printf("%d primeiros numeros da sequencia de Fibonacci:\n", limite);
		int i;
		for (i = 0; i < tamanhoBuffer; i++) {
			printf("%d ", fibonacciBuffer[i]);
		}
	} else {
		puts("Ocorreu um erro inesperado durante a criacao da thread.");
	}

	return EXIT_SUCCESS;
}

int gerarFibonacci(int n) {
	//Se o n-ésimo número da série já estiver no buffer, retorna o valor do buffer:
	if (n < tamanhoBuffer) {
		return fibonacciBuffer[n];
	}
	int valor;
	if (n == 0) {
		valor = 0;
	} else if (n == 1) {
		valor = 1;
	} else {
		valor = gerarFibonacci(n - 1) + gerarFibonacci(n - 2);
	}
	fibonacciBuffer[n] = valor;
	tamanhoBuffer++;
	return valor;
}

DWORD WINAPI geradorFibonacci(PVOID param) {
	int limite = *(int*) param;
	gerarFibonacci(limite);
	return (DWORD) 0;
}
