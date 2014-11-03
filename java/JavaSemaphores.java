
import java.io.*;
import java.util.concurrent.Semaphore;
import java.lang.Object;



     class Reader implements Runnable{
        private Semaphore mutex, writeBlock;
        private int id;
        private PrintStream pr;
        private volatile static int readcount;
        
        public Reader(Semaphore mutex,Semaphore writeBlock, int id, PrintStream pr){
            this.pr = pr;
            this.id = id;
            this.writeBlock = writeBlock;
            this.mutex = mutex;
        }
        
        public void run(){
            
            // print non critical message
            pr.println("Reader " + id + " is attempting to access the critical section..");
            
            // aquire mutex
            try{
            mutex.acquire();
            }catch(InterruptedException ie){}
            
            readcount++;
            
            if (readcount == 1){           
                try{
                    writeBlock.acquire();
                }catch(InterruptedException ie){}
            }
            
            // release the murtex
            mutex.release();
            
            //print critical message
            pr.println("Reader " + id + " is reading");
            try{
            Thread.sleep((long)Math.random()*1000);
            }catch(Exception e){}
            
            // aquire mutex
            try{
            mutex.acquire();
            }catch(InterruptedException ie){}
            
            readcount--;
            
            if (readcount == 0){           
                
                    writeBlock.release();
                
            }
            
            //release the mutex
            mutex.release();
            
              pr.println("Reader " + id + " is done reading!");
          
            
            
        }
    }
    
    
    
    class Writer implements Runnable{
        
        private Semaphore writeBlock;
        private int id;
        private PrintStream pr;
        
        //construct the writer object
        public Writer(Semaphore writeBlock, int id, PrintStream pr){
            this.pr = pr;
            this.id = id;
            this.writeBlock = writeBlock;
        }
        
        // run the writer code
        public void run(){

            // print non critical message
            pr.println("Writer " + id + " is attempting to access the critical section..");
            
            // attempt to enter the critical section
            try{
                writeBlock.acquire();
            }catch(InterruptedException ie){}
            
            // pretend to write
            pr.println("Writer " + id + " is writing" );
            
            try{
            Thread.sleep((long)Math.random()*1000);
            }catch(Exception e){}            
            
            // exit the critical section
            pr.println("Writer " + id + " is done writing!");
            writeBlock.release();
            
            
          
            
        }
    
    }
    
    
    public class JavaSemaphores extends Object {

    public static void main(String[] args) {
        int readNum = 0;
        int writeNum = 0;
        Semaphore writeBlock = new Semaphore(1,true);
        Semaphore mutex = new Semaphore(1,true);
        
        
        for (int i =0; i<9; i++){
            
            if(i%2 == 1) new Thread (new Reader(mutex,writeBlock,readNum++,System.out)).start();
            else new Thread (new Writer(writeBlock,writeNum++,System.out)).start(); 
            
        }
        
    }
    
}
