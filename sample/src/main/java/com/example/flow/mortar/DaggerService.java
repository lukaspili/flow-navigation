package com.example.flow.mortar;

import android.content.Context;

/**
 * @author Lukasz Piliszczuk <lukasz.pili@gmail.com>
 */
public class DaggerService {

    public static final String SERVICE_NAME = DaggerService.class.getName();

    /**
     * Caller is required to know the type of the component for this context.
     */
    @SuppressWarnings("unchecked") //
    public static <T> T getDaggerComponent(Context context) {
        return (T) context.getSystemService(SERVICE_NAME);
    }
}
