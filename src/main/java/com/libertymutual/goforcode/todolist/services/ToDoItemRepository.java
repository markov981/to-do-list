package com.libertymutual.goforcode.todolist.services;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import com.libertymutual.goforcode.todolist.models.ToDoItem;



@Service
public class ToDoItemRepository {

    private int nextId = 1;
    private String FileName = "todo.csv";
    

    /**
     * Get all the items from the file. 
     * @return A list of the items. If no items exist, returns an empty list.
     */
    public List<ToDoItem> getAll() throws FileNotFoundException, IOException {
    	
    	List<ToDoItem> arr = new ArrayList<ToDoItem>();    	
    	
    	try (FileReader fr = new FileReader(FileName);) 
    	{	    	
	    	Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(fr);    	
	    	for (CSVRecord itrt : records) 
	    	{	
	    	    ToDoItem toDoIt = new ToDoItem();
	    		    	
	    		toDoIt.setId(Integer.valueOf(itrt.get(0)));                  
	    		toDoIt.setText(itrt.get(1));                                             
	    		toDoIt.setComplete(Boolean.getBoolean(itrt.get(2)));
	    		
	    	    arr.add(toDoIt);	    	   
    	    }   	
    	}
        return arr;  
    }

    
    /**
     * Assigns a new id to the ToDoItem and saves it to the file.
     * @param item The to-do item to save to the file.
     */
    public void create(ToDoItem item) throws IOException {    	

    	item.setId(nextId);       
    	nextId += 1;
    	   	
		FileWriter wrt = new FileWriter(FileName, true);	// Using this Constructor: FileWriter(File file, boolean append), note the 2nd parameter	
        CSVPrinter printer = CSVFormat.DEFAULT.print(wrt);
        
        String[] arr = new String[3];
	        arr[0] = String.valueOf(item.getId());
	        arr[1] = item.getText();
	        arr[2] = String.valueOf(item.isComplete());  
		    
		printer.printRecord(arr); 
		printer.close();
    }

    
    
    
    
    /**
     * Gets a specific ToDoItem by its id.
     * @param id The id of the ToDoItem.
     * @return The ToDoItem with the specified id or null if none is found.
     * @throws IOException 
     */
    public ToDoItem getById(int id) throws IOException {
		 
    	/* CSVParser:
		    - Parses CSV files per specified format 
		    - Supports many formats via specification of CSVFormat
		    - The parser works record-wise
		    - What we use is 'parse(String, CSVFormat)'
	    
	       CSVRecord
	         - get(int i) returns a record value by its index     */
    	
		    ToDoItem item = new ToDoItem();
	    	FileReader fr = new FileReader(FileName); 	    		    	
		    Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(fr);    	
		    	
		      for (CSVRecord itrt : records) {	
		    	  
	    		  if(id == Integer.valueOf(itrt.get(0)))  
	    		  {
	    			  item.setId(id);
	    			  item.setText(itrt.get(1));
	    			  item.setComplete(Boolean.getBoolean(itrt.get(2)));
	    	      } 
		      }
    	 return item;
    }


      
    /**
     * Updates the given to-do item in the file.
     * @param item The item to update.
     * @throws IOException 
     */
    public void update(ToDoItem item) throws IOException {

    	// 1. Update the state: true/false
	    List<ToDoItem> arr = new ArrayList<ToDoItem>();
	    arr = getAll();
        
        for (ToDoItem itrt : arr) {	    	  
    		  if(item.getId() == itrt.getId()) 
    		  { 
    			  item.setComplete(item.isComplete()); 
    			  break;
    		  }   			  
	    }
	    
	    
	    // 2. Write it all back, overwriting	    
		FileWriter wrt = new FileWriter(FileName);		
        CSVPrinter printer = CSVFormat.DEFAULT.print(wrt);
        
        for (ToDoItem itrt : arr) {
          String[] tempArr = new String[3];
	        tempArr[0] = String.valueOf(itrt.getId());
	        tempArr[1] = itrt.getText();
	        tempArr[2] = String.valueOf(itrt.isComplete());  
		    
		  printer.printRecord(tempArr); 			
        }
        //arr.clear();        // ??????
        printer.close();    // ??????
    }
}