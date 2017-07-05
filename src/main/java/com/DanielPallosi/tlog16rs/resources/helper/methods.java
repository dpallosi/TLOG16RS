/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DanielPallosi.tlog16rs.resources.helper;

import com.DanielPallosi.tlog16rs.beans.*;
import com.DanielPallosi.tlog16rs.core.timelogger.Task;
import com.DanielPallosi.tlog16rs.core.timelogger.TimeLogger;
import com.DanielPallosi.tlog16rs.core.timelogger.WorkDay;
import com.DanielPallosi.tlog16rs.core.timelogger.WorkMonth;
import com.DanielPallosi.tlog16rs.core.timelogger.exceptions.*;
import java.time.LocalTime;
import java.util.List;

/**
 *
 * @author lysharnie
 */
public class methods {
    public static WorkDay addNewDay(WorkDayRB day, TimeLogger tl) throws NegativeMinutesOfWorkException, FutureWorkException, EmptyTimeFieldException, NotNewMonthException, WeekendNotEnabledException, NotNewDateException, NotTheSameMonthException{
        WorkDay newDay = new WorkDay((long)(day.getRequiredHours()*60), day.getYear(), day.getMonth(), day.getDay());
        for (WorkMonth wm : tl.getMonths())
            if (wm.Date().getYear() == newDay.ActualDay().getYear() && wm.Date().getMonthValue() == newDay.ActualDay().getMonthValue()){
                wm.addWorkDay(newDay);
                return newDay;
            }
        
        WorkMonth newMonth = new WorkMonth(newDay.ActualDay().getYear(), newDay.ActualDay().getMonthValue());
        newMonth.addWorkDay(newDay);
        tl.addMonth(newMonth);
        return newDay;
    }
    
    public static Task addNewTask(StartTaskRB task, TimeLogger tl) throws InvalidTaskIdException, NotExpectedTimeOrderException, NoTaskIdException, EmptyTimeFieldException, NotSeparatedTimesException, FutureWorkException, NegativeMinutesOfWorkException, WeekendNotEnabledException, NotNewDateException, NotTheSameMonthException, NotNewMonthException{
        Task newTask = new Task(task.getTaskId(), task.getStartTime(), task.getStartTime(), task.getComment());
        for (WorkMonth wm : tl.getMonths()){
            for (WorkDay wd : wm.getDays()){
                if (wd.ActualDay().getYear() == task.getYear() && 
                        wd.ActualDay().getMonthValue() == task.getMonth() && 
                        wd.ActualDay().getDayOfMonth() == task.getDay()){
                    wd.addTask(newTask);
                    updateStats(wm, wd, newTask);
                    return newTask;
                }
            }
            if (wm.Date().getYear() == task.getYear() && 
                    wm.Date().getMonthValue() == task.getMonth()){
                WorkDay newDay = new WorkDay(450, task.getYear(), task.getMonth(), task.getDay());
                wm.addWorkDay(newDay);
                newDay.addTask(newTask);
                updateStats(wm, newDay, newTask);
                return newTask;
            }
        }
        WorkMonth newMonth = new WorkMonth(task.getYear(), task.getMonth());
        WorkDay newDay = new WorkDay(450, task.getYear(), task.getMonth(), task.getDay());
        newDay.addTask(newTask);
        newMonth.addWorkDay(newDay);
        tl.addMonth(newMonth);
        updateStats(newMonth, newDay, newTask);
        return newTask;
    }
    
    private static void updateStats(WorkMonth wm, WorkDay wd, Task t) throws EmptyTimeFieldException{
        wd.setSumPerDay(t.getMinPerTask());
        wm.setSumPerMonth(t.getMinPerTask());
    }
    
    public static List<WorkDay> listSpecifiedMonth(int year, int month, TimeLogger tl) throws NotNewMonthException{
        for (WorkMonth wm : tl.getMonths())
            if (wm.Date().getYear() == year && wm.Date().getMonthValue() == month)
                return wm.getDays();
        
        WorkMonth newMonth = new WorkMonth(year, month);
        tl.addMonth(newMonth);
        return newMonth.getDays();
    }
    
    public static List<WorkMonth> listMonths(TimeLogger tl){
        return tl.getMonths();
    }
    
    public static List<Task> listSpecifiedDay(int year, int month, int day, TimeLogger tl) throws FutureWorkException, EmptyTimeFieldException, NegativeMinutesOfWorkException, WeekendNotEnabledException, NotNewDateException, NotTheSameMonthException, NotNewMonthException{
        for (WorkMonth wm : tl.getMonths()){
            if (wm.Date().getYear() == year && wm.Date().getMonthValue() == month){
                for (WorkDay wd : wm.getDays()){
                    if (wd.ActualDay().getDayOfMonth() == day){
                        return wd.getTasks();
                    }
                }
                WorkDay newDay = new WorkDay(450, year, month, day);
                wm.addWorkDay(newDay);
                return newDay.getTasks();
            }
        }
        WorkMonth newMonth = new WorkMonth(year, month);
        WorkDay newDay = new WorkDay(450, year, month, day);
        newMonth.addWorkDay(newDay);
        tl.addMonth(newMonth);
        return newDay.getTasks();
    }
    
    public static Task finishSpecifiedTask(FinishingTaskRB task, TimeLogger tl) throws EmptyTimeFieldException, NotSeparatedTimesException, NotExpectedTimeOrderException, NegativeMinutesOfWorkException, FutureWorkException, WeekendNotEnabledException, NotNewDateException, NotTheSameMonthException, NotNewMonthException, InvalidTaskIdException, NoTaskIdException{
        Task finishedTask = new Task(task.getTaskId(), task.getStartTime(), task.getEndTime(), "");
        for (WorkMonth wm : tl.getMonths()){
            if (wm.Date().getYear() == task.getYear() &&
                    wm.Date().getMonthValue() == task.getMonth()){
                for (WorkDay wd : wm.getDays()){
                    if (wd.ActualDay().getDayOfMonth() == task.getDay()){
                        for (Task t : wd.getTasks()){
                            if (task.getTaskId().equals(t.getTaskId()) &&
                                    task.getStartTime().equals(t.getStartTime().toString())){
                                t.setEndTime(task.getEndTime());
                                updateStats(wm, wd, t);
                                return t;
                            }
                        }
                        wd.addTask(finishedTask);
                        updateStats(wm, wd, finishedTask);
                        return finishedTask;
                    }
                }
                WorkDay newDay = new WorkDay(task.getYear(), task.getMonth(), task.getDay());
                newDay.addTask(finishedTask);
                wm.addWorkDay(newDay);
                updateStats(wm, newDay, finishedTask);
                return finishedTask;
            }
        }
        
        WorkMonth newMonth = new WorkMonth(task.getYear(), task.getMonth());
        WorkDay newDay = new WorkDay(task.getYear(), task.getMonth(), task.getDay());
        newDay.addTask(finishedTask);
        newMonth.addWorkDay(newDay);
        tl.addMonth(newMonth);
        updateStats(newMonth, newDay, finishedTask);
        return finishedTask;
    }
    
    private static void updateStatsBeforeModify(WorkMonth wm, WorkDay wd, Task t) throws EmptyTimeFieldException{
        wm.setSumPerMonth(-1 * t.getMinPerTask());
        wd.setSumPerDay(-1 * t.getMinPerTask());
    }
    
    public static Task modifyTask(ModifyTaskRB task, TimeLogger tl) throws InvalidTaskIdException, NotExpectedTimeOrderException, NoTaskIdException, EmptyTimeFieldException, NotSeparatedTimesException, NegativeMinutesOfWorkException, FutureWorkException, WeekendNotEnabledException, NotNewDateException, NotTheSameMonthException, NotNewMonthException{
        Task newTask = new Task("0000","00:00", "23:59", "");
        for (WorkMonth wm : tl.getMonths()){
            if (task.getYear() == wm.Date().getYear() && task.getMonth() == wm.Date().getMonthValue()){
                for (WorkDay wd : wm.getDays()){
                    if (wd.ActualDay().getDayOfMonth() == task.getDay()){
                        for (Task t : wd.getTasks()){
                            if (t.getTaskId().equals(task.getTaskId()) && 
                                    t.getStartTime().toString().equals(task.getStartTime())){
                                updateStatsBeforeModify(wm, wd, t);
                                if (!task.getNewTaskId().isEmpty())
                                    newTask.setTaskId(task.getNewTaskId());
                                else newTask.setTaskId(task.getTaskId());
                                if (!task.getNewStartTime().isEmpty())
                                    newTask.setStartTime(task.getNewStartTime());
                                else newTask.setStartTime(task.getStartTime());
                                if (!task.getNewEndTime().isEmpty())
                                    newTask.setEndTime(task.getNewEndTime());
                                else newTask.setEndTime(t.getEndTime());
                                if (!task.getNewComment().isEmpty())
                                    newTask.setComment(task.getNewComment());
                                else newTask.setComment(t.getComment());
                                updateStats(wm, wd, newTask);
                                wd.getTasks().remove(t);
                                wd.getTasks().add(newTask);
                                return newTask;
                            }
                        }
                        if (!task.getNewTaskId().isEmpty())
                            newTask.setTaskId(task.getNewTaskId());
                        else newTask.setTaskId(task.getTaskId());
                        if (!task.getNewStartTime().isEmpty())
                            newTask.setStartTime(task.getNewStartTime());
                        else newTask.setStartTime(task.getStartTime());
                        if (!task.getNewEndTime().isEmpty())
                            newTask.setEndTime(task.getNewEndTime());
                        else newTask.setEndTime(newTask.getStartTime());
                        if (!task.getNewComment().isEmpty())
                            newTask.setComment(task.getNewComment());
                        else newTask.setComment(newTask.getComment());
                        wd.addTask(newTask);
                        updateStats(wm, wd, newTask);
                        return newTask;
                    }
                }
                WorkDay newDay = new WorkDay(task.getYear(), task.getMonth(), task.getDay());
                if (!task.getNewTaskId().isEmpty())
                    newTask.setTaskId(task.getNewTaskId());
                else newTask.setTaskId(task.getTaskId());
                if (!task.getNewStartTime().isEmpty())
                    newTask.setStartTime(task.getNewStartTime());
                else newTask.setStartTime(task.getStartTime());
                if (!task.getNewEndTime().isEmpty())
                    newTask.setEndTime(task.getNewEndTime());
                else newTask.setEndTime(newTask.getStartTime());
                if (!task.getNewComment().isEmpty())
                    newTask.setComment(task.getNewComment());
                else newTask.setComment(newTask.getComment());
                newDay.addTask(newTask);
                wm.addWorkDay(newDay);
                updateStats(wm, newDay, newTask);
                return newTask;
            }
        }
        WorkMonth newMonth = new WorkMonth(task.getYear(), task.getMonth());
        WorkDay newDay = new WorkDay(task.getYear(), task.getMonth(), task.getDay());
        if (!task.getNewTaskId().isEmpty())
            newTask.setTaskId(task.getNewTaskId());
        else newTask.setTaskId(task.getTaskId());
        if (!task.getNewStartTime().isEmpty())
            newTask.setStartTime(task.getNewStartTime());
        else newTask.setStartTime(task.getStartTime());
        if (!task.getNewEndTime().isEmpty())
            newTask.setEndTime(task.getNewEndTime());
        else newTask.setEndTime(newTask.getStartTime());
        if (!task.getNewComment().isEmpty())
            newTask.setComment(task.getNewComment());
        else newTask.setComment(newTask.getComment());
        newDay.addTask(newTask);
        newMonth.addWorkDay(newDay);
        updateStats(newMonth, newDay, newTask);
        tl.addMonth(newMonth);
        return newTask;
    }
    
    public static void deleteTask(DeleteTaskRB task, TimeLogger tl) throws EmptyTimeFieldException{
        for (WorkMonth wm : tl.getMonths())
            if (wm.Date().getYear() == task.getYear() &&
                    wm.Date().getMonthValue() == task.getMonth())
                for (WorkDay wd : wm.getDays())
                    if (wd.ActualDay().getDayOfMonth() == task.getDay())
                        for (Task t : wd.getTasks())
                            if (t.getTaskId().equals(task.getTaskId()) && 
                                    t.getStartTime().toString().equals(task.getStartTime())){
                                updateStatsBeforeModify(wm, wd, t);
                                wd.getTasks().remove(t);
                                break;
                            }
    }
    
    public static void deleteMonths(TimeLogger tl){
        tl.getMonths().removeAll(tl.getMonths());
    }
}
