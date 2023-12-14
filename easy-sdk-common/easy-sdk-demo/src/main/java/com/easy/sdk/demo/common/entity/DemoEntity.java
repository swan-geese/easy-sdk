package com.easy.sdk.demo.common.entity;

import com.easy.sdk.common.web.crypto.Crypto;
import com.easy.sdk.common.web.crypto.CryptoTypeEnum;
import com.easy.sdk.common.web.sign.Sign;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * @author zy
 * @date Created in 2023/12/7 7:34 PM
 * @description
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DemoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private Integer age;

    @Crypto(CryptoTypeEnum.RSA)
    @Sign
    private String pwd;

}
