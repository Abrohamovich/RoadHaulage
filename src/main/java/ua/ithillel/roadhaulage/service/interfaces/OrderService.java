package ua.ithillel.roadhaulage.service.interfaces;

import org.apache.catalina.authenticator.SavedRequest;
import org.apache.commons.math3.analysis.function.Add;
import ua.ithillel.roadhaulage.entity.*;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderService {
    void save(Order order);
    void delete(long id);
    List<Order> findOrdersByCustomerId(long id);
    List<Order> findOrdersByCourierId(long id);
    List<Order> findAll();
    void update(Order order);
    Optional<Order> findById(long id);
    List<Order> returnOtherPublishedOrders(long id);
    Order createOrder(User user, OrderStatus orderStatus, Address deliveryAddress,
                      Address departureAddress, String additionalInfo, String weight,
                      String weightUnit, String dimensions, String dimensionsUnit,
                      String cost, String currency, Date createdAt, Set<OrderCategory> orderCategories);

}
