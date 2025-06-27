package com.inMemoryRedis.In_Memory_Redis;

public class ValueWrapper {
    private String value;
    private Long expiryTimeMillis;

    public ValueWrapper(String value) {
        this.value = value;
        this.expiryTimeMillis = null;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getExpiryTimeMillis() {
        return expiryTimeMillis;
    }

    public void setExpiryTimeMillis(Long expiryTimeMillis) {
        this.expiryTimeMillis = expiryTimeMillis;
    }

    public boolean isExpired() {
         return expiryTimeMillis!=null && System.currentTimeMillis()>expiryTimeMillis;

    }
}
