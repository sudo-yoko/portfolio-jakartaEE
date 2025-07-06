package com.example.presentation;

import org.apache.wicket.Page;
import org.apache.wicket.csp.CSPDirective;
import org.apache.wicket.csp.CSPDirectiveSrcValue;
import org.apache.wicket.protocol.http.WebApplication;

import com.example.presentation.pages.home.HomePage;

public class MyApplication extends WebApplication {

    @Override
    protected void init() {
        super.init();

        getCspSettings().blocking()
                .add(CSPDirective.STYLE_SRC, CSPDirectiveSrcValue.SELF)
                .add(CSPDirective.STYLE_SRC, "https://fonts.googleapis.com/css")
                .add(CSPDirective.FONT_SRC, "https://fonts.gstatic.com");

        getRequestCycleListeners().add(new MyApplicationExceptionHandler());
        getApplicationSettings().setInternalErrorPage(HomePage.class);

    }

    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }

}
