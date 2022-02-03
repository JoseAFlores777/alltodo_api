package com.kodigo.alltodo_api.model.enums;

import lombok.Getter;

@Getter
public enum Gender {
        M("Male"),
        F("Female");

        private final String gender;

        Gender(String gender) {
            this.gender = gender;
        }
}
