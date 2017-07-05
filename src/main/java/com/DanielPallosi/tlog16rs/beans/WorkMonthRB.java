/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DanielPallosi.tlog16rs.beans;

/**
 *
 * @author lysharnie
 */
public class WorkMonthRB {
    private int year;
    private int month;
    
    public WorkMonthRB(){
        
    }
    
    public void setYear(int year){
        this.year = year;
    }
    
    public void setMonth(int month){
        this.month = month;
    }
    
    public int getYear(){
        return year;
    }
    
    public int getMonth(){
        return month;
    }
}
