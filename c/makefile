CC = gcc -pthread -lrt
OBJ = readWrite.o
BIN = readwrite

$(BIN): $(OBJ)
	$(CC) $(OBJ) -o $(BIN)

.c.o:
	$(CC) -c $<

clean:
	rm *.o $(BIN)

