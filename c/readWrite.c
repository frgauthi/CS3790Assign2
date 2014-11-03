/* 
	This program Demonstrates the reader/writer problem using semephores in C..
   	Author: Frankie Gauthier CS3790 Assign 2
	

*/


#include <stdio.h>
#include <pthread.h>
#include </usr/include/semaphore.h>
#include <stdlib.h>

#define MAXACTORS 4


//function prototypes
void *Reader(void*);
void *Writer(void*);



// declare semephores	
sem_t writeBlock; // to block readers when there are too many readers in the critical section
sem_t mutex;  // to block everyone when a writer enters the critical section
int readcount = 0;



// - returns a pointer to void and takes pointer to void as a param
// - attempts to enter the critical section and read allowing multiple simultaneous readers
// - blocks if there are too many readers or a writer in the critical section

void *Reader(void *arg){

	// cast param as an int 
	int id = (int)arg;

	//print non critical message
	printf("Reader %d attempting to enter critical section..\n", id);

	//attempt to enter the critical section
	sem_wait(&mutex);
	readcount++;
	if(readcount == 1) sem_wait(&writeBlock);
	sem_post(&mutex);	
	
	// pretend to read
	printf("Reader %d is Reading\n",id);
	sleep((rand()%3 +1)); 

	//exit the critical section
	sem_wait(&mutex);
	readcount--;
	if(readcount == 0) sem_post(&writeBlock);
	sem_post(&mutex);
	
		
	printf("Reader %d is done reading!\n",id);

return NULL;

}




// - returns a pointer to void and takes pointer to void as a param
// - attempts to enter the critical section and write 
// - blocks if there are any readers or a writer in the critical section

void *Writer(void *arg){

	int id = (int)arg;
	
	// print non critical message
	printf("Writer %d is attempting to enter the critical section..\n",id);

	// attempt to enter the critical section
	sem_wait(&writeBlock);
	
	//pretend to write
	printf("Writer %d is writing\n",id);
	sleep(rand()%3+1);
	
	//exit the critical section
	sem_post(&writeBlock);

	printf("Writer %d is done Writing!\n",id);

return NULL;

}


		

void main(){
	
	// declare threads and index for loops
	pthread_t writeThread[MAXACTORS], readThread[MAXACTORS];
	int index=0;

	//initialize semaphores
	sem_init(&writeBlock, 0, 1);
	sem_init(&mutex, 0, 1);
	
	// start and alternate the read/write threads
	for(index = 1;index<=MAXACTORS;index++){
	
		pthread_create(&writeThread[index],NULL,Writer,(void*)index);	

	}

	for(index = 1; index<=MAXACTORS; index++){

			
		 pthread_create(&readThread[index],NULL,Reader,(void *)index);

	}

	pthread_exit(NULL);	
}

