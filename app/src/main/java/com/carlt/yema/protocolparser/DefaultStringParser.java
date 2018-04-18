package com.carlt.yema.protocolparser;

public class DefaultStringParser extends BaseParser {

    public DefaultStringParser(ResultCallback callback) {
        super(callback);
    }

    @Override
    protected void parser() throws Exception {
        String data = mJson.get("data").toString();
        mBaseResponseInfo.setValue(data);
    }

}
