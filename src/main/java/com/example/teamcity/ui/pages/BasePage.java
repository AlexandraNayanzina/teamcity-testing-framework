package com.example.teamcity.ui.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.elements.BasePageElement;
import java.util.List;


import java.time.Duration;
import java.util.function.Function;

public abstract class BasePage {
    protected  static final Duration BASE_WAITING = Duration.ofSeconds(30);
    protected  static final Duration LONG_WAITING = Duration.ofMinutes(1);

    protected <T extends BasePageElement> List<T> generatePageelements(
            ElementsCollection collection, Function<SelenideElement, T> creator)
    {
        return collection.stream().map(creator).toList();
    }
}
