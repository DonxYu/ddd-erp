package cn.geekhalo.codegen.soaupdater;


import cn.geekhalo.common.ddd.support.AggregateRoot;

public abstract class BaseAbstractEntityUpdater<T extends BaseAbstractEntityUpdater>{
    public void accept(AggregateRoot target) {
    }
    public BaseAbstractEntityUpdater() {
    }
}
