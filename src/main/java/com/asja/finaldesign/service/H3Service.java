package com.asja.finaldesign.service;

import com.asja.finaldesign.common.dto.GeoJson;
import com.asja.finaldesign.common.dto.geo.Feature;
import com.asja.finaldesign.common.dto.geo.Geometry;
import com.asja.finaldesign.common.dto.geo.Properties;
import com.uber.h3core.H3Core;
import com.uber.h3core.util.GeoCoord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author ASJA
 * @Create 2022-04-21 17:05
 */
@Service
public class H3Service {

    @Autowired
    private H3Core h3Core;

    @Qualifier("threadExecutor")
    @Autowired
    private Executor executor;

    /**
     * 返回覆盖该坐标点的六边形网格索引
     * @param lng
     * @param lat
     * @param zoom  层级 1-15
     * @return
     */
    public  long getCenterHexagon(double lng,double lat,int zoom){
        return h3Core.geoToH3(lat,lng,zoom);
    }

    /**
     * 获取一个六边形栅栏
     * @param index
     * @return
     */
    public  List<GeoCoord> getHexagonCoordsByH3Index(long index){
        return h3Core.h3ToGeoBoundary(index);
    }

    /**
     *
     * @param lng
     * @param lat
     * @param zoom
     * @param layer
     * @return
     */
    public  GeoJson getHexagonsGeoJson(double lng,double lat,int zoom,int layer){
        long h3Cen = h3Core.geoToH3(lat, lng, zoom);
        List<List<Long>> kRingDistances = h3Core.kRingDistances(h3Cen, layer);
        List<List<GeoCoord>> ans = new ArrayList<>();

        // 异步操作
        CompletableFuture[] completableFutures = kRingDistances.stream().map(item -> CompletableFuture.runAsync(() -> {
            List<List<GeoCoord>> lists = item.stream().map(this::getHexagonCoordsByH3Index).collect(Collectors.toList());
            ans.addAll(lists);
        }, executor)).toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(completableFutures).join();

        GeoJson geoJson = new GeoJson();
        List<Feature> featureList = new ArrayList<>();

        ans.forEach(polygons->{
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
