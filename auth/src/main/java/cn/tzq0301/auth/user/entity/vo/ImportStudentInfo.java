package cn.tzq0301.auth.user.entity.vo;

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
public class ImportStudentInfo {
    private String id;

    private String username;

    private String role; // STUDENT / ...

    private Integer sex;

    private String phone;

    private String email;

    private String identity;
}
