/*
 * fibonacci.c
 *
 *  Created on: 27/08/2010
 *      Author: 0303585
 */
#include <stdio.h>
#include <stdlib.h>

/**
 * Para executar o programa � necess�rio passar como argumento um inteiro especificando
 * a quantidade de elementos da sequ�ncia de Fibonacci a serem impressos.
 */
int main(int argc, char** argv) {
	if (argc != 2) {
		printf("%s", "N�mero de parametros inv�lido!");
		return -1;
	}
	int qtd = strtoll(argv[1], NULL, 10);
	int n = qtd - 1;
	if (qtd <= 0) {
		printf("%s", "Parametro inv�lido! O n�mero deve ser maior que zero!");
		return -1;
	}

	printf("O(s) %d primeiro(s) n�mero(s) da sequ�ncia de Fibonacci s�o:", qtd);

	int ant2 = 0;
	int ant1 = 1;
	int i;
	int fib = 0;
	if (n >= 0) {
		fib = ant2;
		printf(" %d", fib);
	}
	if (n >= 1) {
		fib = ant1;
		printf(" %d", fib);
	}
	if (n >= 2) {
		for (i = 2; i <= n; i++) {
			fib = ant2 + ant1;
			printf(" %d", fib);
			ant2 = ant1;
			ant1 = fib;
		}
	}

	return 0;
}
