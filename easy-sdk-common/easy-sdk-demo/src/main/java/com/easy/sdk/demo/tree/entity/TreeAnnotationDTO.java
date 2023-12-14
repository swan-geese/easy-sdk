package com.easy.sdk.demo.tree.entity;

import com.easy.sdk.common.annotation.TreeAnnotation;
import com.easy.sdk.common.constant.TreeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author zy
 * @version 1.0
 * @date Created in 2023/11/22 5:26 PM
 * @description 递归树注解方式dto
 */

@Data
@Accessors(chain = true)
@NoArgsConstructor(staticName = "of")
public class TreeAnnotationDTO {

    @TreeAnnotation(TreeEnum.ID)
    private Integer id; // id

    @TreeAnnotation(TreeEnum.PARENT_ID)
    private Integer parentId;// 父id

    @TreeAnnotation(TreeEnum.SUB_LIST)
    private List<TreeDTO> subList;// 子集
}
