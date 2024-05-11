package vn.hoidanit.jobhunter.domain;

import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.domain.dto.Meta;

@Getter
@Setter
public class ResultPaginationDTO {
    private Meta meta;
    private Object result;

}
