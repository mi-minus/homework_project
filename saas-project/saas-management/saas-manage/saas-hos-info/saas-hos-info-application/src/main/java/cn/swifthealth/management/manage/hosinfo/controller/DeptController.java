package cn.swifthealth.management.manage.hosinfo.controller;

import java.util.Optional;

import cn.swifthealth.common.jsonRes.ResponseMessage;
import cn.swifthealth.common.tools.PageResult;
import cn.swifthealth.management.manage.hosinfo.entity.BaseDeptInfo;
import cn.swifthealth.management.manage.hosinfo.service.DeptService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * <p> 科室公共Controller </p>
 *
 * @author <a href="mailto:minus@swifthealth.cn">minus</a>
 * @version v1.0
 * @since 1.0.0
 */
@RestController
@RequestMapping("/hos/dept")
@AllArgsConstructor
public class DeptController {

    private final DeptService deptService;

    @GetMapping("/list")
    public ResponseMessage findAllDept(@RequestParam(value = "deptCode", required = false) String deptCode,
                                       @RequestParam Integer page,
                                       @RequestParam Integer size) {
        return ResponseMessage.success(PageResult.of(page, size, deptService.findAllDeptByCond(deptCode, page, size)));
    }

    @PostMapping("/add")
    public ResponseMessage addDept(@RequestBody BaseDeptInfo deptInfo) {
        if (deptService.addDeptInfo(deptInfo)) {
            return ResponseMessage.success();
        }
        return ResponseMessage.error();
    }

    @PostMapping("/update")
    public ResponseMessage updateDept(@RequestBody BaseDeptInfo deptInfo) {
        if (deptService.updateDeptInfo(deptInfo)) {
            return ResponseMessage.success();
        }
        return ResponseMessage.error();
    }

    @DeleteMapping("/delete")
    public ResponseMessage delDept(@RequestParam String deptCode) {
        return ResponseMessage.success(deptService.delDeptInfo(deptCode));
    }

}
