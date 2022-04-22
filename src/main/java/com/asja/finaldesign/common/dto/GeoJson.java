package com.asja.finaldesign.common.dto;

import com.asja.finaldesign.common.constant.COLOR;
import com.asja.finaldesign.common.dto.geo.Feature;
import com.asja.finaldesign.common.dto.geo.Geometry;
import com.asja.finaldesign.common.dto.geo.Properties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class GeoJson {
    private String type = "FeatureCollection";
    private List<Feature> features;

    /**
     *  odHeatData 转 GeoJson
     * @param odInOutHeatDataList
     * @return
     */
    public static GeoJson odHeatDataCovToGeoJson(List<ODInOutHeatData> odInOutHeatDataList){
        GeoJson geoJson = new GeoJson();
        int len = odInOutHeatDataList.size();
        int colorSize = COLOR.COLOR_LEVEL_LIST.size();
        int index = 0;
        List<Feature> featureList = new ArrayList<>();
        for(int i = 0;i<len;i++){
            Feature feature = new Feature();
            Properties properties = new Properties();
            properties.setFill(COLOR.COLOR_LEVEL_LIST.get(index++/(len/colorSize+1)))
                    .setOriginColId(odInOutHeatDataList.get(i).getOriginColId())
                    .setOriginRowId(odInOutHeatDataList.get(i).getOriginRowId())
                    .setDestColId(odInOutHeatDataList.get(i).getDestinationColId())
                    .setDestRowId(odInOutHeatDataList.get(i).getDestinationRowId())
                    .setFlow(odInOutHeatDataList.get(i).getFlow());
            String coords = odInOutHeatDataList.get(i).getCoordinates();
            String[] cds = coords.substring(3,coords.length()-3).split("],\\[");
            List<List<Float>> coord = new ArrayList<>();
            for (String str:cds){
                String[] split = str.split(",");
                List<Float> tmp = new ArrayList<Float>(){{
                    add(Float.valueOf(split[0]));
                    add(Float.valueOf(split[1]));
                }};
                coord.add(tmp);
            }
            Geometry<List<List<Float>>> geometry = new Geometry<>();
            geometry.setType("Polygon");
            geometry.setCoordinates(new ArrayList<List<List<Float>>>(){{
                add(coord);
            }});
            feature.setProperties(properties).setGeometry(geometry);
            featureList.add(feature);
        }
        geoJson.setFeatures(featureList);
        return geoJson;

    }

}
