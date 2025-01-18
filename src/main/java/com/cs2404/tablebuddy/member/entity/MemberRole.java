package com.cs2404.tablebuddy.member.entity;

import com.cs2404.tablebuddy.common.exception.CustomBusinessException;
import com.cs2404.tablebuddy.common.exception.ErrorCode;

import java.util.HashMap;
import java.util.Map;

public enum MemberRole {
    CUSTOMER,
    OWNER;

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

        throw new CustomBusinessException(ErrorCode.INVALID_ROLE);
    }

    public String toRoleString() {
        return "ROLE_" + name();
    }


}
