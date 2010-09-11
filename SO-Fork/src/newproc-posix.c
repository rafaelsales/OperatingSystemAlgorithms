#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

int main(int argc, char **argv) {
	if (argc != 2) {
		printf("%s", "Número de parametros inválido!\n");
		return -1;
	}

	pid_t pid;

	/* fork a child process */
	pid = fork();

	if (pid < 0) {
		fprintf(stderr, "Fork falhou!\n");
		exit(-1);
	} else if (pid == 0) {
		execlp("./Release/Fibonacci", "Fibonacci", argv[1], NULL);
	} else {
		/* parent will wait for the child to complete */
		wait(NULL);
		printf("Child completo\n");
		exit(0);
	}

	return 0;
}

