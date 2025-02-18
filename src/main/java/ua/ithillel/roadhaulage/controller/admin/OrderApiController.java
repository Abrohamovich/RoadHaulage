package ua.ithillel.roadhaulage.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.ithillel.roadhaulage.dto.OrderDto;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;

import java.util.List;

@RestController
@RequestMapping("/admin/api/order")
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderService orderService;

    @GetMapping("/find-all")
    public ResponseEntity<List<OrderDto>> findAll(@RequestParam int page,
                                                  @RequestParam int pageSize) {
        return ResponseEntity.ok(orderService.findAllPageable(page, pageSize));
    }

    @GetMapping("/find-by")
    public ResponseEntity<OrderDto> findBy(@RequestParam int id) {
        return ResponseEntity.of(orderService.findById(id));
    }

    @PostMapping("/update")
    public ResponseEntity<OrderDto> update(@RequestBody OrderDto orderDto) {
        return ResponseEntity.ok(orderService.save(orderDto));
    }
}
