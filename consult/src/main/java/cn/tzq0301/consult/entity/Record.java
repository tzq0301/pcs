package cn.tzq0301.consult.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tzq0301
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Record {
    private String detail;

    // 咨询状态（完成咨询 COMPLETED 为 0、旷约 ABSENT 为 1、请假 LEAVE 为 2、
    // 结案 FINISHED 为 3、仍需继续咨询 CONTINUE 为 4、尚未开始 WAIT 为 5）
    private Integer status;

    private String day;

    private Integer from;

    private String address;
}
