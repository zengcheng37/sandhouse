package com.zengcheng.sandhouse.common.config.antMatcherRule;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zengcheng
 */
@Data
@Validated
public class CustomAntMatchRuleVO implements Serializable {

    private static final long serialVersionUID = 4193886087904797308L;

    /**
     * 规则id
     */
    @NotNull
    private String id;
    /**
     * 是否需要具备某权限
     */
    private List<String> roles = new ArrayList<>();
    /**
     * 此条规则涉及路径
     */
    @NotNull
    private List<CustomAntMatchRulePathVO> paths = new ArrayList<>();
    /**
     * 该条规则是校验通过还是拒绝
     */
    private Boolean ifPass = true;

}
