package cn.geekhalo.erp.dto.product;

import lombok.Data;

import java.util.List;

@Data
public class AssembleSpecDto {
    private List<SpecAttachInfo> attaches;
    private Long parentId;
}
