/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DanielPallosi.tlog16rs.beans;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author lysharnie
 */

@Getter
@Setter
@NoArgsConstructor
public class FinishingTaskRB {
    private int year = LocalDate.now().getYear();
    private int month = LocalDate.now().getMonthValue();
    private int day = LocalDate.now().getDayOfMonth();
    private String taskId = "";
    private String startTime = "00:00";
    private String endTime = "00:00";
    
}
