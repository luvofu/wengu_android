package com.culturebud.annotation;

import com.culturebud.contract.BasePresenter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by XieWei on 2016/11/22.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PresenterInject {
    Class<? extends BasePresenter> value();
}
