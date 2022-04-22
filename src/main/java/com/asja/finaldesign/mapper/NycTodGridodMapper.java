package com.asja.finaldesign.mapper;

import com.asja.finaldesign.common.dto.ODInOutHeatData;
import com.asja.finaldesign.entity.NycTodGridod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ASJA
 * @since 2022-03-30
 */
public interface NycTodGridodMapper extends BaseMapper<NycTodGridod> {

    List<ODInOutHeatData> getODInOutHeatData(
            @Param("dynaId") int dynaId,
            @Param("rowId") int rowId,
            @Param("colId") int colId,
            @Param("inOrOut") int inOrOut);

    List<ODInOutHeatData> getAllFlow(
            @Param("dynaId") int dynaId,
            @Param("inOrOut") int inOrOut);
}
