import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BQ {
    //shared data structure that has an infinite capacity
    private final static Queue<Integer> pipe=new LinkedList<>();
    private final static int CAPACITY=4;
    private final static ReentrantLock lock= new ReentrantLock();
    private final static Condition notEmpty=lock.newCondition();
    private final static Condition notFull=lock.newCondition();
    static class Producer implements Runnable{
        private int data;
        public Producer(int k){
            data=k;
        }
        public void run(){
            lock.lock();
            try{
                while(pipe.size()==CAPACITY){
                    notFull.await();

                }
                pipe.add(data);
                notEmpty.signal();
            }catch(InterruptedException e ){
                e.printStackTrace();
            }finally{
              lock.unlock();
            }
        }

    }

    static class Consumer implements Runnable{
        public void run(){
            lock.lock();
            try{
                while(pipe.isEmpty()){
                    notEmpty.await();
                }    
                System.out.println(pipe.remove());
                notFull.signal();
            }catch(InterruptedException e){
                e.printStackTrace();
            }finally{
                lock.unlock();
            }
        }
    }


    public static void main(String[] args) {
        ExecutorService es= Executors.newFixedThreadPool(4);
        for(int i=0;i<10;i++){
            es.submit(new Consumer());
            es.submit(new Producer(i));
        }
        es.shutdown();
        
    }


}
