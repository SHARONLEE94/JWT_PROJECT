package com.lab.jwtcore.store;

import com.lab.jwtcore.model.AuthTokens;
import com.lab.jwtcore.model.RefreshTokenRecord;
import com.lab.jwtcore.model.User;

import java.util.HashMap;
import java.util.Map;

public class RefreshStore {
    // 싱글톤
    private static final RefreshStore INSTANCE = new RefreshStore();

    private final Map<String, RefreshTokenRecord> store = new HashMap<>();

    private RefreshStore() {}

    public static RefreshStore getInstance() {
        return INSTANCE;
    }

    public void save(RefreshTokenRecord record) {
        store.put(record.getUserId(), record);
    }

    public RefreshTokenRecord findByUserId(String userId) {
        return store.get(userId);
    }

    public void delete(String userId) {
        store.remove(userId);
    }

    public void clear() {
        store.clear();
    }
}
