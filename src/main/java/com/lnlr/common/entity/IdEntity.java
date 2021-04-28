package com.lnlr.common.entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author:leihfei
 * @description 包含id的参数
 * @date:Create in 15:36 2018/9/6
 * @email:leihfein@gmail.com
 */
@ApiModel(value = "只包含id的对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdEntity implements Serializable {

    @NotBlank(message = "信息不允许为空")
    private String id;


}
