package com.asja.finaldesign.common.dto.geo;

import com.uber.h3core.util.GeoCoord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Description
 * @Author ASJA
 * @Create 2022-04-29 18:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Polygon {

    private long idx;

    private List<GeoCoord> geoCoords;

    public static Polygon genPolygon(List<GeoCoord> geoCoords,long idx){
        return new Polygon(idx,geoCoords);
    }
}
