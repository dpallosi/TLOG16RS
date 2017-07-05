package com.DanielPallosi.tlog16rs.resources;

import com.DanielPallosi.tlog16rs.beans.*;
import com.DanielPallosi.tlog16rs.core.timelogger.Task;
import com.DanielPallosi.tlog16rs.core.timelogger.TimeLogger;
import com.DanielPallosi.tlog16rs.core.timelogger.WorkDay;
import com.DanielPallosi.tlog16rs.core.timelogger.WorkMonth;
import com.DanielPallosi.tlog16rs.core.timelogger.exceptions.*;
import com.DanielPallosi.tlog16rs.resources.helper.Comparator;
import com.DanielPallosi.tlog16rs.resources.helper.methods;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;


@Path("/timelogger/workmonths")
@Slf4j
public class TLOG16RSResource {
    TimeLogger tl = TimeLogger.getInstance();
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<WorkMonth> listMonths() {
        Collections.sort(tl.getMonths(), new Comparator());
        return methods.listMonths(tl);
    }
    
    @POST //curl -i -X POST -H "Content-Type: application/json" -d "{\"year\":\"2016\",\"month\":\"8\"}" http://localhost:8080/timelogger/workmonths
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public WorkMonth addNewMonth(WorkMonthRB month){
        WorkMonth workmonth = new WorkMonth(month.getYear(), month.getMonth());
        try {
            tl.addMonth(workmonth);
        } catch (NotNewMonthException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return workmonth;
    }
    
    @Path("/workdays") //curl -i -X POST -H "Content-Type: application/json" -d "{\"year\":\"2000\",\"month\":\"9\",\"day\":\"18\",\"requiredHours\":\"350\"}" http://localhost:8080/timelogger/workmonths/workdays
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public WorkDay createNewDay(WorkDayRB day){
        try {
            return methods.addNewDay(day, tl);
        } catch (NegativeMinutesOfWorkException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FutureWorkException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EmptyTimeFieldException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotNewMonthException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WeekendNotEnabledException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotNewDateException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotTheSameMonthException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @Path("/workdays/tasks/start") //curl -i -X POST -H "Content-Type: application/json" -d "{\"year\":\"2000\",\"month\":\"9\",\"day\":\"18\",\"taskId\":\"1234\",\"startTime\":\"07:30\",\"comment\":\"asd\"}" http://localhost:8080/timelogger/workmonths/workdays/tasks/start
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Task startTask(StartTaskRB task){
        try {
            return methods.addNewTask(task, tl);
        } catch (InvalidTaskIdException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotExpectedTimeOrderException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoTaskIdException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EmptyTimeFieldException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotSeparatedTimesException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FutureWorkException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NegativeMinutesOfWorkException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WeekendNotEnabledException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotNewDateException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotTheSameMonthException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotNewMonthException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @Path("/{year}/{month}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<WorkDay> getDaysOfTheMonth(@PathParam(value = "year") int year, @PathParam(value = "month") int month){
        try {
            return methods.listSpecifiedMonth(year, month, tl);
        } catch (NotNewMonthException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @Path("/{year}/{month}/{day}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Task> getTasksOfTheDay(@PathParam(value = "year") int year, @PathParam(value = "month") int month, @PathParam(value = "day") int day){
        try {
            return methods.listSpecifiedDay(year, month, day, tl);
        } catch (FutureWorkException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EmptyTimeFieldException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NegativeMinutesOfWorkException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WeekendNotEnabledException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotNewDateException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotTheSameMonthException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotNewMonthException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @Path("/workdays/tasks/finish") //curl -i -X PUT -H "Content-Type: application/json" -d "{\"year\":\"2000\",\"month\":\"9\",\"day\":\"18\",\"taskId\":\"1234\",\"startTime\":\"07:30\",\"endTime\":\"08:30\"}" http://localhost:8080/timelogger/workmonths/workdays/tasks/finish
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Task finishTask(FinishingTaskRB task){
        try {
            return methods.finishSpecifiedTask(task, tl);
        } catch (EmptyTimeFieldException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotSeparatedTimesException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotExpectedTimeOrderException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NegativeMinutesOfWorkException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FutureWorkException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WeekendNotEnabledException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotNewDateException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotTheSameMonthException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotNewMonthException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidTaskIdException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoTaskIdException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    @Path("/workdays/tasks/modify") //curl -i -X PUT -H "Content-Type: application/json" -d "{\"year\":\"2000\",\"month\":\"9\",\"day\":\"18\",\"taskId\":\"1234\",\"startTime\":\"07:30\",\"newTaskId\":\"4444\"}" http://localhost:8080/timelogger/workmonths/workdays/tasks/modify
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Task modifyTask(ModifyTaskRB task){
        try {
            return methods.modifyTask(task, tl);
        } catch (InvalidTaskIdException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotExpectedTimeOrderException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoTaskIdException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EmptyTimeFieldException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotSeparatedTimesException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NegativeMinutesOfWorkException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FutureWorkException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WeekendNotEnabledException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotNewDateException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotTheSameMonthException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotNewMonthException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @Path("/workdays/tasks/delete")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteTask(DeleteTaskRB task){
        try {
            methods.deleteTask(task, tl);
        } catch (EmptyTimeFieldException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Path("/deleteall")
    @PUT
    public void deleteMonths(){
        methods.deleteMonths(tl);
    }
}


