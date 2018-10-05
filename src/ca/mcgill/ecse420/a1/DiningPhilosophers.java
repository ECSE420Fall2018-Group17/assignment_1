package ca.mcgill.ecse420.a1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophers {
	
	public static void main(String[] args) {

		int numberOfPhilosophers = 5;
		Philosopher[] philosophers = new Philosopher[numberOfPhilosophers];
		Object[] chopsticks = new Object[numberOfPhilosophers];

		//creating 5 chopsticks
		for(int i=0;i<chopsticks.length;i++){
			chopsticks[i] = new Object();
		}

		//To run the threads
		ExecutorService executorService = Executors.newFixedThreadPool(numberOfPhilosophers);

		for(int i=0;i<philosophers.length;i++ ){
			//creating chopstick objects to pass to philopsopher constructor
			Object leftChopstick = chopsticks[i];
			Object rightChopstick = chopsticks[(i+1)%chopsticks.length];

			//create a philosopher and execute it in a thread
			//This implementation results in a deadlock
			//Since all 5 might pick up the left or chopstick first and keep waiting
			//resulting in circular wait
			philosophers[i] = new Philosopher(leftChopstick,rightChopstick);
			executorService.execute(philosophers[i]);
		}

	}

	public static class Philosopher implements Runnable {

		//Each philosopher has a chopstick on either side
		private Object leftChopstick;
		private Object rightChopstick;

		//Constructor
		public Philosopher(Object leftChopstick, Object rightChopstick){
			this.leftChopstick = leftChopstick;
			this.rightChopstick = rightChopstick;
		}

		//function that tells if philosopher is thinking, eating or getting chopsticks
		private void action(String action) throws InterruptedException {
			System.out.println(Thread.currentThread().getName()+": "+action);
			Thread.sleep(((int) (Math.random() * 10)));
		}

		@Override
		public void run() {
			try{
				while(true){
					action("Thinking");
					synchronized (leftChopstick){
						action("Got Left Chopstick");
						synchronized (rightChopstick){
							action("Got Right Chopstick");
							action("Eating");
							action("Leave Right Chopstick");
						}
						action("Leave Left Chopstick");
					}

				}
			}catch (InterruptedException exception){
				exception.printStackTrace();
			}
		}

	}

}
