package ua.ithillel.roadhaulage.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ua.ithillel.roadhaulage.dto.OrderCategoryDto;
import ua.ithillel.roadhaulage.entity.OrderCategory;
import ua.ithillel.roadhaulage.mapper.OrderCategoryMapper;
import ua.ithillel.roadhaulage.repository.OrderCategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderCategoryServiceDefaultTests {
    @InjectMocks
    private OrderCategoryServiceDefault orderCategoryService;
    @Mock
    private OrderCategoryRepository orderCategoryRepository;
    @Mock
    private OrderCategoryMapper orderCategoryMapper;
    private OrderCategoryDto orderCategoryDto;
    private OrderCategory orderCategory;

    @BeforeEach
    void init() {
        orderCategoryDto = new OrderCategoryDto();
        orderCategoryDto.setId(1L);
        orderCategoryDto.setName("Test");
        orderCategory = new OrderCategory();
        orderCategory.setId(1L);
        orderCategory.setName("Test");
    }

    @Test
    void save() {
        when(orderCategoryMapper.toEntity(orderCategoryDto)).thenReturn(orderCategory);
        when(orderCategoryRepository.save(orderCategory)).thenReturn(orderCategory);
        when(orderCategoryMapper.toDto(orderCategory)).thenReturn(orderCategoryDto);

        OrderCategoryDto result = orderCategoryService.save(orderCategoryDto);

        assertNotNull(result);
        assertEquals(orderCategoryDto, result);
    }

    @Test
    void findAll_returnsEmptyList() {
        when(orderCategoryRepository.findAll()).thenReturn(List.of());

        List<OrderCategoryDto> result = orderCategoryService.findAll();

        assertEquals(0, result.size());
    }

    @Test
    void findAll_returnsFullList() {
        when(orderCategoryMapper.toDto(orderCategory)).thenReturn(orderCategoryDto);

        when(orderCategoryRepository.findAll()).thenReturn(List.of(orderCategory));

        List<OrderCategoryDto> result = orderCategoryService.findAll();

        assertEquals(1, result.size());
        assertEquals(orderCategoryDto, result.getFirst());
    }

    @Test
    void findAllPageable_returnsEmptyList() {
        when(orderCategoryRepository.findAll(PageRequest.of(0, 1)))
                .thenReturn(new PageImpl<>(List.of()));

        List<OrderCategoryDto> result = orderCategoryService.findAllPageable(0, 1);

        assertEquals(0, result.size());
    }

    @Test
    void findAllPageable_returnsFullList() {
        when(orderCategoryMapper.toDto(orderCategory)).thenReturn(orderCategoryDto);

        when(orderCategoryRepository.findAll(PageRequest.of(0, 1)))
                .thenReturn(new PageImpl<>(List.of(orderCategory)));

        List<OrderCategoryDto> result = orderCategoryService.findAllPageable(0, 1);

        assertEquals(1, result.size());
        assertEquals(orderCategoryDto, result.getFirst());
    }

    @Test
    void findById_returnsPresentOptional() {
        when(orderCategoryRepository.findById(anyLong())).
                thenReturn(Optional.of(orderCategory));
        when(orderCategoryMapper.toDto(orderCategory)).thenReturn(orderCategoryDto);

        Optional<OrderCategoryDto> result = orderCategoryService.findById(1);

        assertNotNull(result);
        assertTrue(result.isPresent());
    }

    @Test
    void findById_returnsEmptyOptional() {
        when(orderCategoryRepository.findById(anyLong())).
                thenReturn(Optional.empty());

        Optional<OrderCategoryDto> result = orderCategoryService.findById(1);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByName_returnsOptionalOfOrderCategory() {
        when(orderCategoryMapper.toDto(orderCategory)).thenReturn(orderCategoryDto);
        when(orderCategoryRepository.findByName(anyString()))
                .thenReturn(Optional.of(orderCategory));

        Optional<OrderCategoryDto> result = orderCategoryService.findByName(anyString());

        assertTrue(result.isPresent());
        assertEquals(orderCategoryDto, result.get());
    }

    @Test
    void findByName_returnsOptionalOfNull() {
        when(orderCategoryRepository.findByName(anyString()))
                .thenReturn(Optional.empty());

        Optional<OrderCategoryDto> result = orderCategoryService.findByName(anyString());

        assertTrue(result.isEmpty());
    }

    @Test
    void createOrderCategorySet_returnsSetOfNewOrderCategory() {
        String categoryNamesString = " test ";

        when(orderCategoryRepository.findByName(anyString())).thenReturn(Optional.empty());

        Set<OrderCategoryDto> result = orderCategoryService.createOrderCategorySet(categoryNamesString);

        assertEquals(1, result.size());
        assertEquals(orderCategoryDto.getName(), result.iterator().next().getName());
    }

    @Test
    void createOrderCategorySet_returnsSetOfExistedOrderCategory() {
        String categoryNamesString = " test ";

        when(orderCategoryRepository.findByName(anyString()))
                .thenReturn(Optional.of(orderCategory));

        Set<OrderCategoryDto> result = orderCategoryService.createOrderCategorySet(categoryNamesString);

        assertEquals(1, result.size());
    }
}