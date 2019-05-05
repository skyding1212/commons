package com.alex.commons.util;

import com.alibaba.fastjson.JSONObject;

public class ResponseUtil {

    public enum STATUS {
        FAIL(-1, "opreate fail"),
        SUCCESS(00, "success"),
        ERROR_PARAMER(01, "paramer error"),
        ERROR_NO_MONNEY(02, "not sufficient funds"),
        BUSY(03, "busy"),
        UNSTART(04, "un start")
        ;

        private Integer code;
        private String des;

        STATUS(Integer code, String des) {
            this.code = code;
            this.des = des;
        }
    }

    public enum MODULE {
        COMMON(0, "COMMON"),
        ACCOUNT(1, "ACCOUNT"),
        REDPACKET(2, "REDPACKET");

        private Integer moduleCode;
        private String module;

        MODULE(Integer moduleCode, String module) {
            this.moduleCode = moduleCode;
            this.module = module;
        }
    }

    public static Object success(final MODULE module, final Object data) {
        return new JSONObject() {{
            put("module", module.module);
            put("moduleCode", module.moduleCode);
            put("CODE", STATUS.SUCCESS.code);
            put("DES", STATUS.SUCCESS.des);
            put("DATA", data);
        }};
    }

    public static Object error(final MODULE module, final STATUS status, final Object data) {
        return new JSONObject() {{
            put("module", module.module);
            put("moduleCode", module.moduleCode);
            put("CODE", status.code);
            put("DES", status.des);
            put("DATA", data);
        }};
    }
}
