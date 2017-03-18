package org.homonoia.echo.bot.event.factory;

import org.homonoia.echo.bot.annotations.RespondTo;
import org.homonoia.echo.bot.event.FilteredEventExpressionEvaluator;
import org.homonoia.echo.bot.event.adapter.RespondToApplicationListenerMethodAdapter;
import org.homonoia.echo.configuration.properties.HipchatProperties;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListenerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 17/03/2017
 */
public class RespondToListenerFactory implements EventListenerFactory, Ordered, ApplicationContextAware {

    private final HipchatProperties hipchatProperties;
    private ApplicationContext applicationContext;
    private final FilteredEventExpressionEvaluator evaluator = new FilteredEventExpressionEvaluator();

    public RespondToListenerFactory(HipchatProperties hipchatProperties) {
        this.hipchatProperties = hipchatProperties;
    }

    @Override
    public boolean supportsMethod(Method method) {
        return AnnotatedElementUtils.hasAnnotation(method, RespondTo.class);
    }

    @Override
    public ApplicationListener<?> createApplicationListener(String beanName, Class<?> type, Method method) {
        return new RespondToApplicationListenerMethodAdapter(beanName, type, method,evaluator,applicationContext,hipchatProperties);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}