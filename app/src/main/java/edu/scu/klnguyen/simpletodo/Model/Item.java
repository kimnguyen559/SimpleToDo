package edu.scu.klnguyen.simpletodo.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Kim Nguyen on 9/24/2016.
 */

// This is an item of the to-do list

public class Item implements Serializable, Comparable<Item>{
    private String name;        // name of task
    private Date dueDate;       // due date
    private String notes;       // notes for the task
    private int priority;       // priority leve: 0: high, 1: medium, 2: low
    private int status;         // status: 0: to-do, 1: done
    private int id;             // id of task

    public Item(String name, Date dueDate, String notes, int priority, int status){
        this.name = name;
        this.dueDate = dueDate;
        this.notes = notes;
        this.priority = priority;
        this.status = status;
    }

    // compare priority level
    public int compareTo(Item item) {
        int diff = this.priority - item.getPriority();
        if (diff > 0)
            return 1;
        else if (diff == 0)
            return 0;
        else
            return -1;
    }

    public boolean equals(Item item) {
        return compareTo(item) == 0;
    }
    public String getName(){
        return name;
    }

    public Date getDueDate(){
        return dueDate;
    }

    public String getNotes(){
        return notes;
    }

    public int getPriority(){
        return priority;
    }

    public int getStatus(){
        return status;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setDueDate(Date dueDate){
        this.dueDate = dueDate;
    }

    public void setNotes(String notes){
        this.notes = notes;
    }

    public void setPriority(int priority){
        this.priority = priority;
    }

    public void setStatus(int status){
        this.status = status;
    }
}
