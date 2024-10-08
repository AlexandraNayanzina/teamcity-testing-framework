package com.example.teamcity.api.annotations;


import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
/**
 * Fields will be filled out with random data
 */
public @interface Random {

}
