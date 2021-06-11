package com.zengcheng.sandhouse.common.config.antMatcherRule;

import lombok.Data;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.validation.ValidationException;
import java.io.Serializable;

/**
 * @author zengcheng
 */
@Data
@Validated
public class CustomAntMatchRulePathVO implements Serializable {

    private static final long serialVersionUID = -3113561875865100435L;

    /**
     * 路径
     */
    private String path;

    /**
     * 方法
     */
    private String method;

    public CustomAntMatchRulePathVO(String text) {
        String[] splitStrArray = text.split(",");
        if (splitStrArray.length < 1 || StringUtils.isEmpty(splitStrArray[0])) {
            throw new ValidationException("Unable to parse AntMatchRulePathVO text '" + text
                    + "'" + ", must be of the form 'path,method' or 'path'");
        }
        this.path = splitStrArray[0];
        this.method = splitStrArray.length > 1 ? splitStrArray[1] : "";
    }

}
