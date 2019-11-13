package cn.geekhalo.codegen.vo;

import cn.geekhalo.common.ddd.support.AbstractEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class BaseAbstractEntityVO {
    @ApiModelProperty(
            value = "数据版本",
            name = "version"
    )
    private int version;
    @ApiModelProperty(
            value = "主键",
            name = "id"
    )
    private Long id;
    @ApiModelProperty(
            value = "创建时间",
            name = "createdAt"
    )
    private Long createdAt;
    @ApiModelProperty(
            value = "修改时间",
            name = "updatedAt"
    )
    private Long updatedAt;

    protected BaseAbstractEntityVO(AbstractEntity source) {
        this.setVersion(source.getVersion());
        this.setId(source.getId());
        this.setCreatedAt(source.getCreatedAt().toEpochMilli());
        this.setUpdatedAt(source.getUpdatedAt().toEpochMilli());
    }

    protected BaseAbstractEntityVO() {
    }
}
