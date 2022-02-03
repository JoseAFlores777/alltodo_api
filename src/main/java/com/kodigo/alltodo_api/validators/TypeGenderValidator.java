package com.kodigo.alltodo_api.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TypeGenderValidator implements ConstraintValidator<TypeGenderConstraint,String> {
    @Override
    public void initialize(TypeGenderConstraint contactNumber) {
    }

    @Override
    public boolean isValid(String gender, ConstraintValidatorContext constraintValidatorContext) {
        return (gender.equals("Male") || gender.equals("Female"));
    }

}
