package com.api_order.middlewares;

public class OrderExecptionHandler extends RuntimeException {
  public OrderExecptionHandler(String message) {
    super(message);
  }
}
