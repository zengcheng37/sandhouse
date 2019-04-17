package com.zengcheng.sandhouse.entity;


import com.zengcheng.sandhouse.constant.ResCodeEnum;

/**
 * 响应信息返回类 工厂类
 * @author zengcheng
 * @date 2019/4/12
 */
public class ResponseEntityFactory {

    /**
     * 成功响应(无任何返回信息)
     * @return
     */
    public static ResponseEntity success(){
        return success(null);
    }

    /**
     * 成功响应(有返回信息)
     * @return
     */
    public static ResponseEntity success(Object resBody){
        ResponseEntity<Object> successRes = new ResponseEntity<>();
        successRes.setResCode(ResCodeEnum.R0.getCode());
        successRes.setResMessage(ResCodeEnum.R0.getMessage());
        successRes.setResBody(resBody);
        return successRes;
    }

    /**
     * 失败异常
     * @param resCodeEnum 失败状态枚举值
     * @return
     */
    public static ResponseEntity error(ResCodeEnum resCodeEnum){
        ResponseEntity errorRes = new ResponseEntity();
        errorRes.setResCode(resCodeEnum.getCode());
        errorRes.setResMessage(resCodeEnum.getMessage());
        return errorRes;
    }

    /**
     * 失败异常
     * @return
     */
    public static ResponseEntity error(String resCode,String resMessage){
        ResponseEntity errorRes = new ResponseEntity();
        errorRes.setResCode(resCode);
        errorRes.setResMessage(resMessage);
        return errorRes;
    }

}
