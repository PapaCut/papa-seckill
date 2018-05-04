package org.papa.product;

import java.io.Serializable;

/**
 * Created by PaperCut on 2018/3/1.
 */
public class ProductDto implements Serializable{
    private final int userId;
    private final int itemId;

    public ProductDto(int userId, int itemId) {
        this.userId = userId;
        this.itemId = itemId;
    }

    public int getUserId() {
        return userId;
    }

    public int getItemId() {
        return itemId;
    }
}
