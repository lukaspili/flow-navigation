package com.example.flow.app;

import android.app.Application;
import android.content.Context;

import com.example.flow.BuildConfig;
import com.example.flow.mortar.DaggerService;
import com.example.flow.rest.RestClient;

import javax.inject.Singleton;

import dagger.Provides;
import mortar.MortarScope;
import timber.log.Timber;

public class App extends Application {

    private MortarScope mortarScope;

    @Override
    public Object getSystemService(String name) {
        return mortarScope.hasService(name) ? mortarScope.getService(name) : super.getSystemService(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        Component component = DaggerApp_Component.builder()
                .module(new Module())
                .build();
        component.inject(this);

        mortarScope = MortarScope.buildRootScope()
                .withService(DaggerService.SERVICE_NAME, component)
                .build("Root");
    }


    @dagger.Component(modules = Module.class)
    @Singleton
    public interface Component {

        RestClient restClient();

        void inject(App app);
    }

    @dagger.Module
    public class Module {

        @Provides
        @Singleton
        Context provideApplicationContext() {
            return getApplicationContext();
        }
    }
}
