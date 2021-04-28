package com.lnlr.pojo.vo.zorro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author leihf
 * @email leihfein@gmail.com
 * @description ng-zorro下拉列表
 * @date 2019-04-10 18:04:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NgZorroListVO {

    /**
     * 名称
     */
    private String text;

    /**
     * id
     */
    private String value;
}
