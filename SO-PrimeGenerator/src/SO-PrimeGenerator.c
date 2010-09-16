/*
 ============================================================================
 Name        : SO-PrimeGenerator.c
 Author      : 
 Version     :
 Copyright   : 
 Description : Hello World in C, Ansi-style
 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

void *generatePrimes(long limite) {
	long i;
	printf("Números primos até %ld: ", limite);
	for (i = 2; i <= limite; ++i) {
		long j;
		int isPrimo = 1;
		for (j = 2; j <= i / 2; ++j) {
			if (i % j == 0) {
				isPrimo = 0;
				break;
			}
		}
		if (isPrimo) {
			printf("%ld ", i);
		}
	}
	return NULL;
}

int main(void) {
	long limite;
	puts("--- Gerador de números primos ---");
	do {
		printf("Informe o limite para procurar números primos: ");
		scanf("%ld", &limite);
		if (limite <= 1) {
			puts("O limite deve ser maior que 1.");
		}
	} while (limite <= 1);

	pthread_attr_t threadAttributes;
	pthread_attr_init(&threadAttributes);

	pthread_t thread;
	pthread_create(&thread, &threadAttributes, (void *(*) (void *)) generatePrimes, (void *) limite);

	pthread_join(thread, NULL);

	return EXIT_SUCCESS;
}
