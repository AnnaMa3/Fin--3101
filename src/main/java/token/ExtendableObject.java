package token;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

import java.util.Map;

public class ExtendableObject {
    public String name;
    public String value;
    private Map<String, String> properties;

    public ExtendableObject(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @JsonAnyGetter
    public Map<String, String> getProperties() {
        return properties;
    }

}
