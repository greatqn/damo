package com.damo.api.controller;

import com.damo.api.base.BaseResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test")
public class TestController {

    @ApiOperation(value = "打个招呼", notes = "")
    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public BaseResponse success() {
        BaseResponse result = BaseResponse.success();
        result.put("test", "test");
        return result;
    }
}
