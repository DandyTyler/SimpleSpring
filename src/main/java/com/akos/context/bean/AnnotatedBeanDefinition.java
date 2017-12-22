package com.akos.context.bean;

public class AnnotatedBeanDefinition implements BeanDefinition{

    private MethodData factoryMethodData;


    private String scope;

    public void setScope(String scope) {
        this.scope = scope;
    }
    @Override
    public String getScope() {
        return scope;
    }

    public void setFactoryMethodData(MethodData factoryMethodData){
        this.factoryMethodData = factoryMethodData;
    }

    @Override
    public MethodData getFactoryMethodData() {
        return this.factoryMethodData;
    }
}
