package com.czareg;

import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.google.common.base.Predicate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

class ClientProxy implements InvocationHandler {
    private final Client originalClient;
    private final Retryer<Object> retryer;

    ClientProxy(Client originalClient) {
        this.originalClient = originalClient;
        retryer = RetryerBuilder.newBuilder()
                .retryIfException(wasExpected())
                .withStopStrategy(StopStrategies.stopAfterAttempt(2))
                .build();
    }

    private Predicate<Throwable> wasExpected() {
        return throwable -> throwable.getCause() instanceof ClientException;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            Callable<Object> originalClientMethodInvocation = () -> method.invoke(originalClient, args);
            return retryer.call(originalClientMethodInvocation);
        } catch (RetryException | ExecutionException e) {
            throw e.getCause().getCause(); //unwrapping ClientException
        }
    }
}