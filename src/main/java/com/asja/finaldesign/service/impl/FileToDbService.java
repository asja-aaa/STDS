package com.asja.finaldesign.service.impl;

import com.asja.finaldesign.common.CommonResult;
import com.asja.finaldesign.common.constant.RESULT;
import com.asja.finaldesign.common.util.TimeUtil;
import com.asja.finaldesign.entity.NycOctoberRec;
import com.asja.finaldesign.service.INycOctoberRecService;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.uber.h3core.H3Core;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Description
 * @Author ASJA
 * @Create 2022-04-29 12:14
 */

@Service
@Slf4j
public class FileToDbService {

    @Autowired
    private INycOctoberRecService recService;


    @Qualifier("threadExecutor")
    @Autowired
    private Executor executor;

    @Autowired
    private  H3Core h3Core;


    public CommonResult CsvFileConvToDb(String filePath,int batch){
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(filePath), "utf-8");
            CSVParser csvParser = new CSVParserBuilder().withSeparator(',').build();
            CSVReader reader = new CSVReaderBuilder(is).withCSVParser(csvParser).build();
            reader.readNext();
            reader.readNext();
            List<String[]> list = new ArrayList<>();
            String[] strs;
            int pivot = 0,sum = 0;
            while ((strs=reader.readNext())!=null){
                list.add(strs);
                pivot++;
                if(pivot>=batch){
                    List<NycOctoberRec> dbList = Collections.synchronizedList(new ArrayList<>());
                    CompletableFuture[] completableFutures = list.stream()
                            .filter(i->i.length==18&&!"".equals(i[1])&&!"".equals(i[5])&&!"".equals(i[6])&&!"".equals(i[9])&&!"".equals(i[10]))
                            .map(line -> CompletableFuture.supplyAsync(
                            () -> getNycOctoberRec(line), executor
                    ).thenAccept(dbList::add))
                            .toArray(CompletableFuture[]::new);
                    CompletableFuture.allOf(completableFutures).join();
                    recService.saveBatch(dbList);
                    list.clear();
                    pivot = 0;
                    sum++;
                    log.info("insert:" +sum*batch+"  records");
                }
            }

        } catch (UnsupportedEncodingException e) {
            log.error(e.toString());
            return CommonResult.failFast(RESULT.UN_SUPPORT,e);
        } catch (FileNotFoundException e) {
            log.error(e.toString());
            return CommonResult.failFast(RESULT.FILE_NOT_FOUNED,e);
        } catch (IOException e) {
            log.error(e.toString());
            return CommonResult.failFast(RESULT.IO_ERROR,e);
        }
        return CommonResult.success(null);
    }

    private  NycOctoberRec getNycOctoberRec(String[] line) {
        long orignH3 = h3Core.geoToH3(Double.parseDouble(line[6].trim()), Double.parseDouble(line[5].trim()), 9);
        long destH3 = h3Core.geoToH3(Double.parseDouble(line[10].trim()), Double.parseDouble(line[9].trim()), 9);
        LocalDateTime localDateTime = TimeUtil.timeStrToLocalDateTime(line[1]);
        int timeInterval = (int) ((localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli()-TimeUtil.TIME_2014_10_1_00_00_00)/TimeUtil.TIME_30_min);
        return NycOctoberRec.builder()
                .time(localDateTime)
                .timeInterval(timeInterval)
                .destGridIdx9(destH3)
                .originGridIdx9(orignH3)
                .destLat(Double.parseDouble(line[10]))
                .destLng(Double.parseDouble(line[9]))
                .originLat(Double.parseDouble(line[6]))
                .originLng(Double.parseDouble(line[5]))
                .build();
    }
}
