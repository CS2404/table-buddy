package com.cs2404.tablebuddy.jongkuk.memberenum;

import java.util.HashMap;
import java.util.Map;

public enum MemberRole {
    CUSTOMER,
    OWNER,
    ADMIN;


    private static final Map<String, MemberRole> roleMap = new HashMap<>();

    static {
        for (MemberRole role : MemberRole.values()) {
            roleMap.put(role.toString(), role);
        }
    }


    public static MemberRole from(String memberRole) {
        if (roleMap.containsKey(memberRole)) {
            return roleMap.get(memberRole);
        }

        throw new IllegalArgumentException();
    }
}
