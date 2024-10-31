package com.amberj.common;

public record FileData(
    String name,
    FileType type
) {
    @Override
    public String toString() {
        return name;
    }
}
