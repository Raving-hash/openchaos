package io.openmessaging.driver.elasticsearch.core;

import java.util.Optional;

public class Document {
    private Optional<String> key;
    private String value;

    public Optional<String> getKey() {
        return key;
    }

    public void setKey(Optional<String> key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
