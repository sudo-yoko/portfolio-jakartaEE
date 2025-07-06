package com.example.presentation;

import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.IRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;

public class MyApplicationExceptionHandler implements IRequestCycleListener {

    @Override
    public IRequestHandler onException(RequestCycle cycle, Exception ex) {
        ex.printStackTrace();
        return IRequestCycleListener.super.onException(cycle, ex);
    }

}
