package cn.geekhalo.codegen.creator;


import cn.geekhalo.common.ddd.support.AggregateRoot;

public abstract class BaseAbstractEntityCreator<T extends BaseAbstractEntityCreator>{
    public void accept(AggregateRoot target) {
    }

    public BaseAbstractEntityCreator() {
    }
}
