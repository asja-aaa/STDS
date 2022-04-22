package com.asja.finaldesign.common.dto.geo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Feature {

    private String type = "Feature";
    private Properties properties;
    private Geometry geometry;
}
