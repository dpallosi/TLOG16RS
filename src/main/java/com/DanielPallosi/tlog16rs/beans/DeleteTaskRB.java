/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DanielPallosi.tlog16rs.beans;

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

public class DeleteTaskRB {
    private int year;
    private int month;
    private int day;
    private String taskId;
    private String startTime;
}
