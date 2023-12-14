package com.easy.sdk.common.apmagent.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName Monitor
 * @Description 监控类实体
 * @Author swan-geese
 * @Date 2023/12/05 17:39
 * @Version 1.0
 **/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Monitor implements Serializable {


    private Long id;

    /**
     * 请求路径
     */
    private String url;

    /**
     * 链路id
     */
    private String traceId;

    /**
     * segmentId
     */
    private String segmentId;

    /**
     * 入参
     */
    private String inParam;

    /**
     * 出参
     */
    private String response;

    /**
     * 响应时间
     */
    private String responseTime;

    /**
     * 链路上级id（上级segmentId）
     */
    private String parentId;

    /**
     * 开始时间
     */
    private Date startDate;

    /**
     * 结苏时间
     */
    private Date endDate;

    public void setResponseTime(Long responseTime) {
        Long h = (responseTime / (1000 * 60 * 60)) % 24;
        Long m = (responseTime / (1000 * 60)) % 60;
        Long s = (responseTime / (1000)) % 60;
        Long ms = (responseTime % 1000);
        String time = "";
        if (h != 0) {
            time = time + h + "h:";
        }
        if (m != 0) {
            time = time + m + "m:";
        }
        if (s != 0) {
            time = time + s + "s:";
        }
        time = time + ms + "ms";

        this.responseTime = time;

    }


}
