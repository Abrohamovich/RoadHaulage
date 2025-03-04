package ua.ithillel.roadhaulage.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.ithillel.roadhaulage.dto.AddressDto;
import ua.ithillel.roadhaulage.service.interfaces.AddressService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/api/address")
@RequiredArgsConstructor
public class AddressApiController {
    private final AddressService addressService;

    @GetMapping("/find-all")
    public ResponseEntity<List<AddressDto>> findAll(@RequestParam int page,
                                                    @RequestParam int pageSize) {
        return ResponseEntity.ok(addressService.findAll(page, pageSize));
    }

    @GetMapping("/find-by")
    public ResponseEntity<AddressDto> findById(@RequestParam long id) {
        return ResponseEntity.of(addressService.findById(id));
    }

    @PostMapping("/update")
    public ResponseEntity<AddressDto> update(@RequestBody AddressDto addressDto) {
        Optional<AddressDto> optionalAddressDto = addressService.findById(addressDto.getId());
        if (optionalAddressDto.isPresent()) {
            return ResponseEntity.ok(addressService.save(addressDto));
        }
        return ResponseEntity.notFound().build();
    }
}
