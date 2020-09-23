package tiane.org.ssm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tiane.org.ssm.entity.SysNotice;
import tiane.org.ssm.service.SysNoticeService;
import tiane.org.ssm.util.ReResponse;
import tiane.org.ssm.util.Result;

@RestController
@RequestMapping("/api/notice")
@Api(value="/api/notice",description="消息通知")
public class SysNoticeController {
    @Autowired
    private SysNoticeService sysNoticeService;

    @GetMapping("/list")
    public Result list(@RequestParam(name = "type")String type,
                    @RequestParam(name = "size",defaultValue = "10") String size,
                    @RequestParam(name = "current",defaultValue = "1") String current){
        Page<SysNotice> page = new Page<SysNotice>(Integer.parseInt(current),Integer.parseInt(size));
        QueryWrapper<SysNotice> wrapper = new QueryWrapper<>();
        wrapper.eq("notice_type",type);
        wrapper.eq("status",0);
        return ReResponse.success(sysNoticeService.page(page,wrapper));
    }
}
