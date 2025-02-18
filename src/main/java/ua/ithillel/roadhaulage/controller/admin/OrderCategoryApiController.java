package ua.ithillel.roadhaulage.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.ithillel.roadhaulage.dto.OrderCategoryDto;
import ua.ithillel.roadhaulage.service.interfaces.OrderCategoryService;

import java.util.List;

@RestController
@RequestMapping("/admin/api/category")
@RequiredArgsConstructor
public class OrderCategoryApiController {
    private final OrderCategoryService orderCategoryService;

    @GetMapping("/find-all")
    public ResponseEntity<List<OrderCategoryDto>> findAll(@RequestParam int page,
                                                          @RequestParam int pageSize) {
        return ResponseEntity.ok(orderCategoryService.findAllPageable(page, pageSize));
    }

    @GetMapping("/find-by")
    public ResponseEntity<OrderCategoryDto> findById(@RequestParam long id) {
        return ResponseEntity.of(orderCategoryService.findById(id));
    }

    @PostMapping("/update")
    public ResponseEntity<OrderCategoryDto> update(@RequestBody OrderCategoryDto orderCategoryDto) {
        return ResponseEntity.ok(orderCategoryService.save(orderCategoryDto));
    }
}