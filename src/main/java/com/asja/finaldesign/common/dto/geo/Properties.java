package com.asja.finaldesign.common.dto.geo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Properties {
    /**
     * color
     */
    private String fill;

    @JSONField(name = "fill-opacity")
    private float fillOpacity = 0.8f;

    private int originRowId;

    private int originColId;

    private int destRowId;

    private int destColId;

    private int flow;
}
