package com.coderscampus.assignment8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;

class Assignment8Test {
	
//    @Test
//    
//    public void getData () {
//  
//        Assignment8 assignment = new Assignment8();
//        List<Integer> numbersList = new ArrayList<>();
//        long startTime = System.currentTimeMillis();
//       
//        for (int i=0; i<1000; i++) {
//
//           numbersList = assignment.getNumbers();
//
//            System.out.println(numbersList);
//
//        }
//        // Tried to implement synchronous method to count frequencies, not the scope of this assignment. 
//        // But was able to do it. If you have time would you please take a look and just explain where I am going wrong.
//        //Thank you :)
//
//    	 Map<Integer, Integer> frequencyMap = new HashMap<>();
//    	 
//         for (Integer n: numbersList)
//         {
//        	 frequencyMap.merge(n, 1, Integer::sum);
//         }
//  
//         for (Entry<Integer, Integer> entry: frequencyMap.entrySet()) {
//             System.out.println(entry.getKey() + " =  " + entry.getValue());
//         }
//        
//        long estimatedTimeMilliSeconds = System.currentTimeMillis() - startTime;
//        long estimatedTimeMinutes =  (estimatedTimeMilliSeconds/1000)/60;
//        System.out.println("Time taken to read data from file:	"+ estimatedTimeMinutes +" minutes");
//        
//    }
    
    @Test
    
    public void should_getData_Asynchronous() {
    	
    	//ExecutorService cpuBoundTask = Executors.newSingleThreadExecutor();
    	//ExecutorService ioBoundTask = Executors.newCachedThreadPool();
    	ExecutorService service = Executors.newFixedThreadPool(20);
    	long startTime = System.currentTimeMillis();
    
    	Assignment8 assignment = new Assignment8();
    	List<CompletableFuture <List<Integer>>> tasks = new ArrayList<>(); 
    	for (int i=0; i<1000; i++) {
    	CompletableFuture<List<Integer>> future = CompletableFuture.supplyAsync(()->assignment.getNumbers(), service);
    		tasks.add(future); 
    	}
    	
   	while (tasks.stream().filter(CompletableFuture::isDone).count()<1000) {}
    	 
    	ConcurrentMap<Integer, Integer> numberFrequencyMap = new ConcurrentHashMap<>();
        for (CompletableFuture<List<Integer>> numbers : tasks) {
            try {
                List<Integer> number = numbers.get();
                	for(int n :number) {
                		 synchronized (numberFrequencyMap) {
                        	 numberFrequencyMap.merge(n, 1, Integer::sum); 	
        				}   
                	}
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        
        for (Entry<Integer, Integer> entry : numberFrequencyMap.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }   	
    	
        long estimatedTimeMilliSeconds = System.currentTimeMillis() - startTime;
        long estimatedTimeMinutes =  (estimatedTimeMilliSeconds/1000)/60;
        System.out.println("Time taken to read data from file:	"+ estimatedTimeMilliSeconds +" millisec");
        System.out.println("Time taken to read data from file:	"+ estimatedTimeMinutes +" Minutes");

    	
    }
    
}
