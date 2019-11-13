package cn.geekhalo.common.model;

import cn.geekhalo.common.constants.BaseEnum;
import cn.geekhalo.common.constants.CodeEnum;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public final class JsonObject<T> {
    @Setter(AccessLevel.PRIVATE)
    private Integer code;
    @Setter(AccessLevel.PRIVATE)
    private String msg;
    @Setter(AccessLevel.PRIVATE)
    private T result;

    private JsonObject(){}

    public static <E> JsonObject<E> success(E e){
        JsonObject<E> object = new JsonObject<>();
        object.setCode(CodeEnum.Success.getCode());
        object.setMsg(CodeEnum.Success.getName());
        object.setResult(e);
        return object;
    }

    public static <E> JsonObject<E> success(E t,String msg){
        JsonObject<E> obj = success(t);
        obj.setMsg(msg);
        return obj;
    }

    public static JsonObject fail(BaseEnum codeEnum){
        JsonObject object = new JsonObject();
        object.setMsg(codeEnum.getName());
        object.setCode(codeEnum.getCode());
        return object;
    }

    public static JsonObject fail(String msg){
        JsonObject object = new JsonObject();
        object.setMsg(msg);
        object.setCode(CodeEnum.Fail.getCode());
        return object;
    }

    public static <E> JsonObject<E> fail(E e,String msg){
        JsonObject<E> object = new JsonObject<>();
        object.setCode(CodeEnum.Fail.getCode());
        object.setMsg(msg);
        object.setResult(e);
        return object;
    }

    public static <E> JsonObject<E> res(BaseEnum codeEnum, E t){
        JsonObject object = new JsonObject();
        object.setMsg(codeEnum.getName());
        object.setCode(codeEnum.getCode());
        object.setResult(t);
        return object;
    }




}
