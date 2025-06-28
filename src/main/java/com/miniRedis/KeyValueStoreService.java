package com.miniRedis;

import com.inMemoryRedis.In_Memory_Redis.ValueWrapper;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class KeyValueStoreService {
  private Map<String, ValueWrapper> store = new ConcurrentHashMap<>();

  public String get(String key) {
    ValueWrapper wrapper = store.get(key);
    if(wrapper==null || wrapper.isExpired()) {
      store.remove(key);
      return null;
    }
    return wrapper.getValue();
  }

  public void set(String key, String value) {
    store.put(key, new ValueWrapper(value));
  }

  public boolean delete(String key) {
    return store.remove(key)!=null;
  }

  public boolean doesExist(String key) {
    ValueWrapper wrapper = store.get(key);
    return wrapper!=null && !wrapper.isExpired();
  }

  public String increment(String key) {
    ValueWrapper wrapper = store.get(key);
    if(wrapper==null || wrapper.isExpired()) {
      wrapper = new ValueWrapper("1");
      store.put(key, wrapper);
      return "1";
    }
    try{
        int val = Integer.parseInt(wrapper.getValue());
        val++;
        wrapper.setValue(String.valueOf(val));
        return wrapper.getValue();
        } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Value for key '" + key + "' is not a valid integer");
    }
  }
  public boolean expired(String key, long seconds) {
     ValueWrapper wrapper = store.get(key);
     if(wrapper==null || wrapper.isExpired()) {
       store.remove(key);
       return false;
     } else {
       long expiry = System.currentTimeMillis()+(seconds*100);
       wrapper.setExpiryTimeMillis(expiry);
       return true;
     }
  }

  public long ttl(String key) {
    ValueWrapper wrapper = store.get(key);
    if(wrapper == null || wrapper.isExpired()) {
      store.remove(key);
      return -2;
    }
    Long expire = wrapper.getExpiryTimeMillis();
    if(expire==null) return -1;
    Long ttl = (expire-System.currentTimeMillis())/1000;
    return Math.max(ttl, 0);
  }
}
