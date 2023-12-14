package com.easy.sdk.demo.tree.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zy
 * @version 1.0
 * @date Created in 2023/12/12 10:42 AM
 * @description hutoolTree
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HutoolTree implements Serializable {

    private Long id;

    private Long parentId;

    private String name;

    private Integer weight;

    private HutoolTree children;
}
