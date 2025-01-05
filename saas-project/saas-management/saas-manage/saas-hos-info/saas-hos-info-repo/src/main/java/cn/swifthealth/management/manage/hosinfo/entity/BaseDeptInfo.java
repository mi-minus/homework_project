package cn.swifthealth.management.manage.hosinfo.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class BaseDeptInfo {
    /**
     * 科室编码
     */
    private String deptCode;

    /**
     * 科室名称
     */
    private String deptName;

    /**
     * 科室类型
     */
    private String deptType;

    /**
     * 科室简介
     */
    private String introduction;

    /**
     * 科室擅长
     */
    private String special;

    /**
     * 状态
     */
    private Integer state;
}
