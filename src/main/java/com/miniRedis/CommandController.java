package com.miniRedis;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class CommandController {
    private final KeyValueStoreService keyValueStoreService;

    public CommandController(KeyValueStoreService keyValueStoreService) {
        this.keyValueStoreService = keyValueStoreService;
    }

    @GetMapping("/get")
    public ResponseEntity<String> get(@RequestParam String key) {
        String value = keyValueStoreService.get(key);
        if(value==null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(value);
    }

    @PostMapping("/set")
    public ResponseEntity<String> set(@RequestParam String key, @RequestParam String value) {
        keyValueStoreService.set(key, value);
        return ResponseEntity.ok("Value set successfully");
    }

    @DeleteMapping("/del")
    public ResponseEntity<String> delete(@RequestParam String key) {
        boolean removed = keyValueStoreService.delete(key);
        return ResponseEntity.ok(removed?"1":"0");
    }

    @GetMapping("/exists")
    public ResponseEntity<String> exists(@RequestParam String key) {
        boolean exists = keyValueStoreService.doesExist(key);
        return ResponseEntity.ok(exists?"1":"0");
    }

    @PostMapping("/incr")
    public ResponseEntity<String> increment(@RequestParam String key) {
        try{
            String response = keyValueStoreService.increment(key);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/expire")
    public ResponseEntity<String>  expire(@RequestParam String key, @RequestParam long seconds) {
       boolean response = keyValueStoreService.expired(key, seconds);
       return ResponseEntity.ok(response?"1":"0");
    }

    @GetMapping("/ttl")
    public ResponseEntity<String> ttl(@RequestParam String key) {
       Long ttl = keyValueStoreService.ttl(key);
       return ResponseEntity.ok(String.valueOf(ttl));
    }

}
