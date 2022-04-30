package com.asja.finaldesign.common.dto;

import com.asja.finaldesign.common.constant.COLOR;
import com.asja.finaldesign.common.dto.geo.Feature;
import com.asja.finaldesign.common.dto.geo.Geometry;
import com.asja.finaldesign.common.dto.geo.Polygon;
import com.asja.finaldesign.common.dto.geo.Properties;
import com.uber.h3core.util.GeoCoord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class GeoJson {
    private String type = "FeatureCollection";
    private List<Feature> features;

    /**
     *  含自定义围栏 odHeatData 转 GeoJson
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
    public static GeoJson  geoCoordsCovToGeoJson(List<Polygon> geoCoords){
        GeoJson geoJson = new GeoJson();
        List<Feature> featureList = new ArrayList<>();

        geoCoords.stream().filter(i->i!=null).forEach(polygons->{
            Feature feature = new Feature();
            Geometry<List<List<Float>>> geometry = new Geometry<>();
            geometry.setType("Polygon");
            List<List<Float>> coord = new ArrayList<>();
            polygons.getGeoCoords().forEach(item->{
                List<Float> li = new ArrayList<Float>(){{
                    add((float) item.lng);
                    add((float) item.lat);
                }};
                coord.add(li);
            });
            coord.add(new ArrayList<Float>(){{
                add((float) polygons.getGeoCoords().get(0).lng);
                add((float) polygons.getGeoCoords().get(0).lat);
            }});
            geometry.setCoordinates(new ArrayList<List<List<Float>>>(){{
                add(coord);
            }});

            feature.setGeometry(geometry);
            feature.setProperties(new Properties().setOriginIdx(polygons.getIdx()));
            featureList.add(feature);
        });
        geoJson.setFeatures(featureList);
        return  geoJson;
    }

    /**
     *
     * @param odInOutHeatDataList
     * @param list
     * @param inOrOut 1: 出度  0：入度
     * @return
     */
    public static GeoJson hexagonAddOdHeatDataConvToGeoJson(List<ODInOutHeatData> odInOutHeatDataList,List<Polygon> list,int inOrOut){
        int len = odInOutHeatDataList.size();
        int colorSize = COLOR.COLOR_LEVEL_LIST.size();
        int index = 0;
        Map<Long,List<GeoCoord>> idxMap = new HashMap<>();
        list.forEach(i->idxMap.put(i.getIdx(),i.getGeoCoords()));
        GeoJson ans = new GeoJson();
        List<Feature> featureList = new ArrayList<>();
        for (ODInOutHeatData item:odInOutHeatDataList){
            long idx = inOrOut==0?item.getDestIdx():item.getOriginIdx();
            if(idxMap.containsKey(idx)){
                Feature feature = new Feature();
                Geometry<List<List<Float>>> geometry = new Geometry<>();
                geometry.setType("Polygon");
                List<List<Float>> coord = new ArrayList<>();
                List<GeoCoord> polygons = idxMap.get(idx);
                polygons.forEach(p->{
                    List<Float> li = new ArrayList<Float>(){{
                        add((float) p.lng);
                        add((float) p.lat);
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
                feature.setProperties(new Properties().setFill(COLOR.COLOR_LEVEL_LIST.get(index/(len/colorSize+1))).setOriginIdx(idx));
                featureList.add(feature);
            }
            index++;
        }

        ans.setFeatures(featureList);
        return  ans;


    }

}
