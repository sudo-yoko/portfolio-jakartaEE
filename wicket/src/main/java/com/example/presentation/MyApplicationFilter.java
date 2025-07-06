package com.example.presentation;

import org.apache.wicket.protocol.http.IWebApplicationFactory;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketFilter;

import jakarta.servlet.annotation.WebFilter;

@WebFilter("/*")
public class MyApplicationFilter extends WicketFilter {

    @Override
    protected IWebApplicationFactory getApplicationFactory() {
        final IWebApplicationFactory applicationFactory = super.getApplicationFactory();
        
        return new IWebApplicationFactory() {

            @Override
            public WebApplication createApplication(WicketFilter filter) {
                return new MyApplication();
            }

            @Override
            public void destroy(WicketFilter filter) {
                applicationFactory.destroy(filter);
            }
        };
    }
}
