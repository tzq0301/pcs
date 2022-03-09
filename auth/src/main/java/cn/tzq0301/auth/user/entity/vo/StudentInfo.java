package cn.tzq0301.auth.user.entity.vo;

import cn.tzq0301.auth.user.entity.vo.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author tzq0301
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentInfo extends UserInfo {
    private Integer studentStatus; // 不在进行为 0、正在初访为 1、正在心理咨询为 2

    public StudentInfo(String userId, String name, String role, Integer sex, LocalDate birthday, String phone, String email, String identity, Integer studentStatus) {
        super(userId, name, role, sex, birthday, phone, email, identity);
        this.studentStatus = studentStatus;
    }
}
