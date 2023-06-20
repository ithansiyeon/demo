package com.example.demo.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutoLoginForm {
    private String reasonType;
    private String otherReason;

    @Override
    public String toString() {
        return "AutoLoginForm{" +
                "reasonType='" + reasonType + '\'' +
                ", otherReason='" + otherReason + '\'' +
                '}';
    }
}
