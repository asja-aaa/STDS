package com.asja.finaldesign.common;


import com.asja.finaldesign.common.constant.RESULT;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult<T> {

    private T body;

    private String info;

    private Integer errCode;

    /**
     * 快速返回成果dto
     * @param body
     * @param info
     * @param <T>
     * @return
     */
    public static <T> CommonResult success(T body, String info){
        return new CommonResult<T>(body,info,0);
    }

    /**
     * 快速返回成果dto
     * @param body
     * @param <T>
     * @return
     */
    public static <T> CommonResult success(T body){
        return new CommonResult<T>(body,RESULT.SUCCESS.getMsg(),RESULT.SUCCESS.getErrorCode());
    }

    /**
     * 快速失败返回
     * @param result
     * @param <T>
     * @return
     */
    public static <T> CommonResult failFast(RESULT result){
        return new CommonResult<T>(null,result.getMsg(),result.getErrorCode());
    }

    /**
     * 快速失败返回
     * @param result
     * @param <T>
     * @return
     */
    public static <T> CommonResult failFast(RESULT result,T body){
        return new CommonResult<T>(body,result.getMsg(),result.getErrorCode());
    }


}
