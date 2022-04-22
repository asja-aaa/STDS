package com.asja.finaldesign.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ODInOutHeatData {
    /**
     *  row [0-14]  column [0-4]  [-1,-1]:all
     */
    private Integer originRowId;
    private Integer originColId;
    private Integer destinationRowId;
    private Integer destinationColId;

    private Integer flow;
    private String coordinates;
}
