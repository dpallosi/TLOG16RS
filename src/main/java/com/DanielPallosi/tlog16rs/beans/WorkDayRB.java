/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DanielPallosi.tlog16rs.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class WorkDayRB {
    private double requiredHours = 7.5;
    private int year = LocalDate.now().getYear();
    private int month = LocalDate.now().getMonthValue();
    private int day = LocalDate.now().getDayOfMonth();
}
