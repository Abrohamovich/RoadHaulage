package ua.ithillel.roadhaulage.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import ua.ithillel.roadhaulage.entity.OrderCategory;
import ua.ithillel.roadhaulage.repository.OrderCategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderCategoryServiceImpTests {
    @Mock
    private OrderCategoryRepository repository;
    @InjectMocks
    private OrderCategoryServiceImp service;

    @Test
    public void saveTest(){
        OrderCategory orderCategory = mock(OrderCategory.class);

        service.save(orderCategory);

        verify(repository, times(1)).save(orderCategory);
    }

    @Test
    public void findAllTest_returnsEmptyList(){
        when(repository.findAll()).thenReturn(List.of());

        List<OrderCategory> result = service.findAll();

        assertEquals(0, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void findAllTest_returnsFullList(){
        when(repository.findAll()).thenReturn(List.of(mock(OrderCategory.class),
                mock(OrderCategory.class), mock(OrderCategory.class)));

        List<OrderCategory> result = service.findAll();

        assertEquals(3, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void findByNameTest_returnsOptionalOfOrderCategory(){
        OrderCategory orderCategory = mock(OrderCategory.class);

        when(repository.findByName(anyString())).thenReturn(Optional.of(orderCategory));

        Optional<OrderCategory> result = service.findByName(anyString());

        assertTrue(result.isPresent());
        verify(repository, times(1)).findByName(anyString());
    }

    @Test
    public void findByNameTest_returnsOptionalOfNull(){
        when(repository.findByName(anyString())).thenReturn(Optional.empty());

        Optional<OrderCategory> result = service.findByName(anyString());

        assertFalse(result.isPresent());
        verify(repository, times(1)).findByName(anyString());
    }

    @Test
    public void createOrderCategorySetTest_returnsSetOfNewOrderCategory(){
        String categoryNamesString = "Grocery, Food, Sport equipment";

        when(repository.findByName(anyString())).thenReturn(Optional.empty());

        Set<OrderCategory> result = service.createOrderCategorySet(categoryNamesString);

        assertEquals(3, result.size());
    }

    @Test
    public void createOrderCategorySetTest_returnsSetOfExistedOrderCategory(){
        String categoryNamesString = "Grocery, Food, Sport equipment";
        OrderCategory orderCategory1 = new OrderCategory();
        orderCategory1.setName("Grocery");
        orderCategory1.setId(1L);
        OrderCategory orderCategory2 = new OrderCategory();
        orderCategory2.setName("Food");
        orderCategory2.setId(2L);
        OrderCategory orderCategory3 = new OrderCategory();
        orderCategory3.setName("Sport Equipment");
        orderCategory3.setId(3L);

        when(repository.findByName("Grocery")).thenReturn(Optional.of(orderCategory1));
        when(repository.findByName("Food")).thenReturn(Optional.of(orderCategory2));
        when(repository.findByName("Sport Equipment")).thenReturn(Optional.of(orderCategory3));

        Set<OrderCategory> result = service.createOrderCategorySet(categoryNamesString);

        assertEquals(3, result.size());
        assertTrue(result.contains(orderCategory1));
        assertTrue(result.contains(orderCategory2));
        assertTrue(result.contains(orderCategory3));
    }

}
