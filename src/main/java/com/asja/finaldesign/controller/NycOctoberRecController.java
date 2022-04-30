package com.asja.finaldesign.controller;


import com.alibaba.fastjson.JSON;
import com.asja.finaldesign.common.CommonResult;
import com.asja.finaldesign.common.dto.GeoJson;
import com.asja.finaldesign.common.dto.ODInOutHeatData;
import com.asja.finaldesign.common.dto.geo.Polygon;
import com.asja.finaldesign.service.H3Service;
import com.asja.finaldesign.service.INycOctoberRecService;
import com.asja.finaldesign.service.impl.FileToDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.asja.finaldesign.common.dto.GeoJson.hexagonAddOdHeatDataConvToGeoJson;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ASJA
 * @since 2022-04-29
 */
@RestController
    @RequestMapping("//nyc-october-rec")
public class NycOctoberRecController {

    @Autowired
    private FileToDbService fileToDbService;

    @Autowired
    private INycOctoberRecService octoberRecService;

    @Autowired
    private H3Service h3Service;

    @GetMapping("filetodb")
    public CommonResult fileToDb(
            @RequestParam(value = "filepath",required = false,defaultValue = "D:\\old\\Learn\\finalDesign\\data\\yellow_tripdata_2014-10.csv") String filePath,
            @RequestParam(value = "batch",required = false,defaultValue = "10000") int batch
            ){
        return fileToDbService.CsvFileConvToDb(filePath,batch);
    }

    /**
     *
     * @param interval 时间id
     * @param inOrOut 1: 出度  0：入度
     * @return
     */
    @GetMapping("/allFlow")
    public String getallFlow(
            @RequestParam(value = "interval",required = false,defaultValue = "1") Integer interval,
            @RequestParam(value = "inOrOut",required = false,defaultValue = "1") Integer inOrOut
    ){
        List<ODInOutHeatData> allFlow = octoberRecService.getHexagonAllFlow(interval, inOrOut);
        int layer = 9;
        List<String> polygons = Arrays.stream("40.8167;-73.9634,40.7990;-73.9174,40.6924;-73.9936,40.7106;-74.0393".split(",")).collect(Collectors.toList());
        List<Polygon> tmp = h3Service.getRegionDivideHexagons(polygons,layer);
        GeoJson geoJson = hexagonAddOdHeatDataConvToGeoJson(allFlow,tmp,inOrOut);
        return JSON.toJSONString(CommonResult.success(geoJson));
    }

}
