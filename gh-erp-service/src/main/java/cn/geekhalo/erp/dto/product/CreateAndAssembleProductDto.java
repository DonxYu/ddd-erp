package cn.geekhalo.erp.dto.product;

import lombok.Data;

import java.util.List;

@Data
public class CreateAndAssembleProductDto extends CreateProductDto{
    List<AttachInfo> attaches;
}
