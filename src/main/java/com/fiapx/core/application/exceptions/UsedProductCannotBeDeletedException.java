package com.fiapx.core.application.exceptions;

public class UsedProductCannotBeDeletedException extends RuntimeException {
    public UsedProductCannotBeDeletedException(String id, Exception cause) {
        super(String.format("Product with id = '%s' is in use and cannot be deleted. If you want to" +
                " avoid future uses of this product you should deactivate it.", id),cause);
    }
}
