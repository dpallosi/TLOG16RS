/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DanielPallosi.tlog16rs.resources.helper;

import com.DanielPallosi.tlog16rs.core.timelogger.WorkMonth;

/**
 *
 * @author lysharnie
 */
public class Comparator implements java.util.Comparator<WorkMonth> {
    @Override
    public int compare(WorkMonth wm1, WorkMonth wm2) {
        return wm1.getDate().compareTo(wm2.getDate());
    }
}
