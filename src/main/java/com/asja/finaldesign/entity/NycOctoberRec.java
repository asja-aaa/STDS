package com.asja.finaldesign.entity;

import com.asja.finaldesign.common.util.TimeUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.uber.h3core.H3Core;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author ASJA
 * @since 2022-04-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class NycOctoberRec implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private LocalDateTime time;

    private Integer timeInterval;

    private Long originGridIdx9;

    private Long destGridIdx9;

    private Double originLat;

    private Double originLng;

    private Double destLat;

    private Double destLng;




}
