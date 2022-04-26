package com.asja.finaldesign.common.dto;

import com.asja.finaldesign.common.constant.COLOR;
import com.asja.finaldesign.common.dto.geo.Feature;
import com.asja.finaldesign.common.dto.geo.Geometry;
import com.asja.finaldesign.common.dto.geo.Properties;
import com.uber.h3core.util.GeoCoord;
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


    /**
     * geoCoords 转 GeoJoson
     * @param geoCoords
     * @return
     */
    public static GeoJson  geoCoordsCovToGeoJson(List<List<GeoCoord>> geoCoords){
        GeoJson geoJson = new GeoJson();
        List<Feature> featureList = new ArrayList<>();

        geoCoords.stream().filter(i->i!=null).forEach(polygons->{
            Feature feature = new Feature();
            Geometry<List<List<Float>>> geometry = new Geometry<>();
            geometry.setType("Polygon");
            List<List<Float>> coord = new ArrayList<>();
            polygons.forEach(item->{
                List<Float> li = new ArrayList<Float>(){{
                    add((float) item.lng);
                    add((float) item.lat);
                }};
                coord.add(li);
            });
            coord.add(new ArrayList<Float>(){{
                add((float) polygons.get(0).lng);
                add((float) polygons.get(0).lat);
            }});
            geometry.setCoordinates(new ArrayList<List<List<Float>>>(){{
                add(coord);
            }});

            feature.setGeometry(geometry);
            feature.setProperties(new Properties().setFillOpacity(0.0f));
            featureList.add(feature);
        });
        geoJson.setFeatures(featureList);
        return  geoJson;
    }

}
