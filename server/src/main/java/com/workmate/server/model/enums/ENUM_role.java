package com.workmate.server.model.enums;

public enum ENUM_role
{
    ADMIN ("ADMIN"),
    COMPANY_ADMIN ("COMPANY_ADMIN"),
    COMPANY_USER ("COMPANY_USER");

    private final String name;

    private ENUM_role(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
