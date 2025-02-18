package ua.ithillel.roadhaulage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ua.ithillel.roadhaulage.dto.OrderCategoryDto;
import ua.ithillel.roadhaulage.entity.OrderCategory;
import ua.ithillel.roadhaulage.mapper.OrderCategoryMapper;
import ua.ithillel.roadhaulage.repository.OrderCategoryRepository;
import ua.ithillel.roadhaulage.service.interfaces.OrderCategoryService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderCategoryServiceDefault implements OrderCategoryService {
    private final OrderCategoryRepository orderCategoryRepository;
    private final OrderCategoryMapper orderCategoryMapper;

    @Override
    public OrderCategoryDto save(OrderCategoryDto orderCategoryDto) {
        OrderCategory saved = orderCategoryRepository.save(orderCategoryMapper.toEntity(orderCategoryDto));
        return orderCategoryMapper.toDto(saved);
    }

    @Override
    public Optional<OrderCategoryDto> findByName(String name) {
        return orderCategoryRepository.findByName(name)
                .map(orderCategoryMapper::toDto);
    }

    @Override
    public List<OrderCategoryDto> findAll() {
        return orderCategoryRepository.findAll()
                .stream()
                .map(orderCategoryMapper::toDto)
                .toList();
    }

    @Override
    public List<OrderCategoryDto> findAllPageable(int page, int pageSize) {
        return orderCategoryRepository.findAll(PageRequest.of(page, pageSize))
                .stream()
                .map(orderCategoryMapper::toDto)
                .toList();
    }

    @Override
    public Optional<OrderCategoryDto> findById(long id) {
        return orderCategoryRepository.findById(id)
                .map(orderCategoryMapper::toDto);
    }

    @Override
    public Set<OrderCategoryDto> createOrderCategorySet(String categoryNamesString) {
        String[] categoryNames = Arrays.stream(categoryNamesString.split(","))
                .map(String::trim)
                .map(category -> Arrays.stream(category.split(" "))
                        .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                        .reduce((a, b) -> a + " " + b).orElse(""))
                .toArray(String[]::new);

        Set<OrderCategoryDto> categories = new HashSet<>();
        for(String name : categoryNames) {
            Optional<OrderCategory> orderCategory = orderCategoryRepository.findByName(name);
            if (orderCategory.isEmpty()){
                OrderCategoryDto orderCategoryDto = new OrderCategoryDto();
                orderCategoryDto.setName(name);
                categories.add(orderCategoryDto);
            } else {
                categories.add(orderCategoryMapper.toDto(orderCategory.get()));
            }
        }
        return categories;
    }


}
