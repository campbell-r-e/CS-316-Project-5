package Project5;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
/*
    You can import any additional package here.
 */
import java.util.concurrent.ThreadLocalRandom;




import static java.lang.Thread.sleep;


public class CallCenter {

    /*
       Total number of customers that each agent will serve in this simulation.
       (Note that an agent can only serve one customer at a time.)
     */
    private static final int CUSTOMERS_PER_AGENT = 5;

    /*
       Total number of agents.
     */
    private static final int NUMBER_OF_AGENTS = 3;

    /*
       Total number of customers to create for this simulation.
     */
    private static final int NUMBER_OF_CUSTOMERS = NUMBER_OF_AGENTS * CUSTOMERS_PER_AGENT;

    /*
      Number of threads to use for this simulation.
     */
    private static final int NUMBER_OF_THREADS = 10;
    private static final BlockingQueue<Integer> serveQueue = new LinkedBlockingQueue<>();
    private static final BlockingQueue<Integer> waitQueue = new LinkedBlockingQueue<>();



    /*
       The Agent class.
     */
    public static class Agent implements Runnable {
        //The ID of the agent
        private final int ID;

        //Feel free to modify the constructor
        public Agent(int i) {
            ID = i;
        }
        /*
        Your implementation must call the method below to serve each customer.
        Do not modify this method.
         */
        public void serve(int customerID) {
            System.out.println("Agent " + ID + " is serving customer " + customerID);
            try {
                /*
                   Simulate busy serving a customer by sleeping for a random amount of time.
                */
                sleep(ThreadLocalRandom.current().nextInt(10, 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            int sCount=0;
            while(sCount<CUSTOMERS_PER_AGENT){
                try{
                    int customer=serveQueue.take();
                    serve(customer);
                    sCount++;
                }catch(InterruptedException e ){
                    e.printStackTrace();
                }
            }
        }
    }

    /*
        The greeter class.
     */
    public static class Greeter implements Runnable{
        private int greetedCount=0;
        
             /*
                Your implementation must call the method below to serve each customer.
                Do not modify this method.
                 */
                public void greet(int customerID) {
                    System.out.println("Greeting customer " + customerID);
                        try {
                            /*
                            Simulate busy serving a customer by sleeping for a random amount of time.
                            */
                            sleep(ThreadLocalRandom.current().nextInt(10, 1000));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                    }
                }
        
                @Override
                public void run() {
                    while(greetedCount<NUMBER_OF_CUSTOMERS){
                        try{
                            Integer customer_num=waitQueue.take();
                            greet(customer_num);
                            serveQueue.put(customer_num);
                            System.out.println(customer_num + " is in serve queue at position "+ serveQueue.size());
                            greetedCount++;
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

    /*
        The customer class.
     */
    public static class Customer implements Runnable {
    //TODO: complete the Customer class
        //The ID of the customer.
        private final int ID;


        //Feel free to modify the constructor
        public Customer (int i){
            ID = i;

        }


        @Override
        public void run() {
            try {
                System.out.println(ID + " has arrived.");
                waitQueue.put(ID); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /*
        Create the greeter and agents tasks first, and then create the customer tasks.
        to simulate a random interval between customer calls, sleep for a random period after creating each customer task.
     */
    public static void main(String[] args){
    


     
            ExecutorService es= Executors.newFixedThreadPool(NUMBER_OF_THREADS);

            es.submit(new Greeter());
            for(int w =0; w<=NUMBER_OF_AGENTS;w++){
              es.submit(new Agent(w));
            }
      
              for(int i=1;i<NUMBER_OF_CUSTOMERS;i++){
                  es.submit(new Customer(i));
                
                 
              }
              es.shutdown();
      
          
        
        
    }

}
