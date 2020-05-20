package cc.youchain.protocol.core.methods.response;

import cc.youchain.protocol.core.Response;

import java.util.Map;

public class YOUSubscribe extends Response<Map<String, Object>> {
    public String getSubscriptionId() {
        Map<String, Object> result = getResult();
        return (String) result.get("ID");
    }
}
