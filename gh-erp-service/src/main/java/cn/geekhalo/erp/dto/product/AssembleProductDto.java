package cn.geekhalo.erp.dto.product;

import lombok.Data;

import java.util.List;

@Data
public class AssembleProductDto {
    List<AttachInfo> attaches;
    private Long parentId;
}
