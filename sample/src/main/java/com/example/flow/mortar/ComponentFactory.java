package com.example.flow.mortar;



/**
 * @author Lukasz Piliszczuk <lukasz.pili@gmail.com>
 */
public interface ComponentFactory<T_ParentComponent> {

    Object createComponent(T_ParentComponent parentComponent);
}
