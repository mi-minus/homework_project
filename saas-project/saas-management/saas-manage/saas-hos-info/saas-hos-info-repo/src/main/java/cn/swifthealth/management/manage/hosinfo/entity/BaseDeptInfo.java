package cn.swifthealth.management.manage.hosinfo.entity;

import lombok.*;

@AllArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class BaseDeptInfo {
    private String deptCode;
    private String deptName;
    private String deptType;
    private String introduction;
    private String special;
    private Integer state;
}
