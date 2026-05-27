package com.hongbang.pojo;


import java.util.Objects;

// 静态内部类 GroupKey
public class GroupKey {
    private final Object id;
    private final Object vault;

    public GroupKey(Object id, Object vault) {
        this.id = id;
        this.vault = vault;
    }

    public Object getId() {
        return id;
    }

    public Object getVault() {
        return vault;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupKey groupKey = (GroupKey) o;
        return Objects.equals(id, groupKey.id) && Objects.equals(vault, groupKey.vault);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vault);
    }

    @Override
    public String toString() {
        return "id=" + id + ", vault=" + vault;
    }
}
