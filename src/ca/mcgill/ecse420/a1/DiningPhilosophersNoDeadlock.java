package ca.mcgill.ecse420.a1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophersNoDeadlock {

    public static void main(String[] args) {

        //Using Reentrantlock for chopsticks to prevent starvation
        int numberOfPhilosophers = 5;
        Philosopher[] philosophers = new Philosopher[numberOfPhilosophers];
        ReentrantLock[] chopsticks = new ReentrantLock[numberOfPhilosophers];

        //creating 5 chopsticks
        for(int i=0;i<chopsticks.length;i++){
            chopsticks[i] = new ReentrantLock();
        }

        //To run the threads
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfPhilosophers);

        for(int i=0;i<philosophers.length;i++ ){
            //creating chopstick objects to pass to philopsopher constructor
            ReentrantLock leftChopstick = chopsticks[i];
            ReentrantLock rightChopstick = chopsticks[(i+1)%chopsticks.length];

            //Avoid deadlock by checking if it is the last philospher
            //if yes, then pick up right chopstick first
            if(i == philosophers.length-1){
                philosophers[i] = new Philosopher(rightChopstick,leftChopstick);
            }
            else {
                philosophers[i] = new Philosopher(leftChopstick, rightChopstick);
            }
            executorService.execute(philosophers[i]);
        }

    }

    public static class Philosopher implements Runnable {

        private final ReentrantLock chopstick1;
        private final ReentrantLock chopstick2;


        //Constructor
        public Philosopher(ReentrantLock leftChopstick, ReentrantLock rightChopstick){
            this.chopstick1 = leftChopstick;
            this.chopstick2 = rightChopstick;
        }

        //function that tells if philosopher is thinking, eating or getting chopsticks
        private void action(String action) throws InterruptedException {
            System.out.println(Thread.currentThread().getName()+": "+action);
            Thread.sleep(((int) (Math.random() * 10)));
        }

        @Override
        public void run() {
            while(true){
                try {
                    action("Thinking");
                    if(chopstick1.tryLock()){
                        try{
                            action("Got Left Chopstick");
                            if(chopstick2.tryLock()){
                                try{
                                    action("Got Right Chopstick");
                                    action("Eating");
                                    action("Leave Right Chopstick");
                                }
                                catch(InterruptedException exception){
                                    exception.printStackTrace();
                                }
                                finally {
                                    chopstick2.unlock();
                                }
                            }
                        }
                        catch(InterruptedException exception){
                            exception.printStackTrace();
                        }
                        finally{
                            chopstick1.unlock();
                        }
                    }
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
