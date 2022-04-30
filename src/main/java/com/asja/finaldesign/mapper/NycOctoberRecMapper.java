package com.asja.finaldesign.mapper;

import com.asja.finaldesign.common.dto.ODInOutHeatData;
import com.asja.finaldesign.entity.NycOctoberRec;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ASJA
 * @since 2022-04-29
 */
public interface NycOctoberRecMapper extends BaseMapper<NycOctoberRec> {
    List<ODInOutHeatData> getHexagonAllFlow(
            @Param("interval") int interval,
            @Param("inOrOut") int inOrOut);
}
