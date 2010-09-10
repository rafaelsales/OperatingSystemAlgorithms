/*
 * fibonacci.c
 *
 *  Created on: 27/08/2010
 *      Author: 0303585
 */
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
	int qtd = strtoll(argv[1], NULL, 10);
	int n = qtd - 1;
	if (qtd <= 0) {
		printf("%s", "Parametro inválido! O número deve ser maior que zero!");
		return -1;
	}

	printf("O(s) %d primeiro(s) número(s) da sequência de Fibonacci são:", qtd);

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
