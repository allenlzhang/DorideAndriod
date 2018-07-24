
package com.carlt.sesame.protocolstack.set;

import com.carlt.sesame.protocolstack.BaseParser;

public class EditPhoneNum111Parser extends BaseParser {

    private String code;

    public String getReturn() {
        return code;
    }

    @Override
    protected void parser() {
        code = mJson.optString("data");

    }
}
