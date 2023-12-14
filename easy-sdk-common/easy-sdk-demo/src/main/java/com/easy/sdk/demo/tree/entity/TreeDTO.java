package com.easy.sdk.demo.tree.entity;

import com.easy.sdk.common.entity.TreeEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.List;

/**
 * @author zy
 * @version 1.0
 * @date Created in 2023/11/22 5:05 PM
 * @description 递归树对象
 */

@Data
@NoArgsConstructor(staticName = "of")
@Accessors(chain = true)
public class TreeDTO implements TreeEntity<TreeDTO> {

    private Integer id; // id
    private Integer parentId;// 父id
    private List<TreeDTO> subList;// 子集

    @Override
    public Serializable getId() {
        return id;
    }

    @Override
    public Serializable getParentId() {
        return parentId;
    }

    @Override
    public void setSubList(List<TreeDTO> subList) {
        this.subList = subList;
    }
}
