package com.dishanm.ignite.order_api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LineItem {
    private String itemName;
    private int itemCount;
}
