package ca.mcgill.ecse420.a1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/** Q4: Deadlock example
 * The following is an example of a deadlock where two tasks hold one lock 
 * each but need two in order to proceed. Task1 acquires lock1 and needs lock2
 * to proceed whereas Task2 acquires lock2 and needs lock1 to proceed. Because
 * the tasks will never release one of the locks before having acquired both,
 * the program becomes deadlocked.
*/
public class DeadLock {
  
  // Instantiate the locks
  private static Lock lock1 = new ReentrantLock();
  private static Lock lock2 = new ReentrantLock();

  public static void main(String args[]) {
    
    
    // create and start the competing threads
    DeadLock deadLock = new DeadLock();
    
    Task1 task1 = deadLock.new Task1();
    Task2 task2 = deadLock.new Task2();
    
    Thread thread1 = new Thread(task1);
    Thread thread2 = new Thread(task2);

    thread1.start();
    thread2.start();

  }

  // Task1 needs lock1 and then lock2 for its execution.
  private class Task1 implements Runnable {
      @Override
      public void run(){
        System.out.println("Task1: trying to acquire lock1...");
        lock1.lock();
        System.out.println("Task1: lock1 acquired!");
        
        System.out.println("Task1: trying to acquire lock2...");
        lock2.lock();
        System.out.println("Task1: lock2 acquired!");
        
        System.out.println("Task1: We are in the critical section!");
        
        System.out.println("Task1: trying to release lock2...");
        lock2.unlock();
        System.out.println("Task1: lock2 released!");
        
        System.out.println("Task1: trying to release lock1...");
        lock1.unlock();
        System.out.println("Task1: lock1 released!");
    }
}
  
//Task2 needs lock2 and then lock1 for its execution.
  private class Task2 implements Runnable {
    @Override
    public void run(){
      System.out.println("Task2: trying to acquire lock2...");
      lock2.lock();
      System.out.println("Task2: lock2 acquired!");
      
      System.out.println("Task2: trying to acquire lock1...");
      lock1.lock();
      System.out.println("Task2: lock1 acquired!");
      
      System.out.println("Task2: We are in the critical section!");
      
      System.out.println("Task2: trying to release lock1...");
      lock1.unlock();
      System.out.println("Task2: lock1 released!");
      
      System.out.println("Task2: trying to release lock2...");
      lock2.unlock();
      System.out.println("Task2: lock2 released!");
    }
  }
}