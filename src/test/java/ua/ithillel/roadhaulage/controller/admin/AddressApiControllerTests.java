//package ua.ithillel.roadhaulage.controller.admin;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import ua.ithillel.roadhaulage.dto.AddressDto;
//import ua.ithillel.roadhaulage.dto.AuthUserDto;
//import ua.ithillel.roadhaulage.entity.UserRole;
//import ua.ithillel.roadhaulage.service.interfaces.AddressService;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.hamcrest.Matchers.hasSize;
//import static org.hamcrest.Matchers.is;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(controllers = AddressApiController.class)
//@AutoConfigureMockMvc(addFilters = false)
//@ExtendWith(MockitoExtension.class)
//public class AddressApiControllerTests {
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper objectMapper;
//    @MockitoBean
//    private AddressService addressService;
//
//    private static final String BASE_URL = "/admin/api/address";
//
//    @BeforeEach
//    void init(){
//        AuthUserDto authUserDto = new AuthUserDto();
//        authUserDto.setId(1L);
//        authUserDto.setRole(UserRole.ADMIN);
//
//        SecurityContextHolder.getContext().setAuthentication(
//                new UsernamePasswordAuthenticationToken(authUserDto, null, authUserDto.getAuthorities())
//        );
//    }
//
//    @Test
//    void findAll_returnsListOfAddressesDto() throws Exception {
//        List<AddressDto> mockAddresses = List.of(
//                new AddressDto(), new AddressDto()
//        );
//
//        when(addressService.findAll(0, 10)).thenReturn(mockAddresses);
//
//        mockMvc.perform(get(BASE_URL + "/find-all")
//                        .param("page", "0")
//                        .param("pageSize", "10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//    }
//
//    @Test
//    void findAll_returnsEmptyList() throws Exception {
//        when(addressService.findAll(0, 10)).thenReturn(List.of());
//
//        mockMvc.perform(get(BASE_URL + "/find-all")
//                        .param("page", "0")
//                        .param("pageSize", "10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(0)))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//    }
//
//    @Test
//    void findById_returnsAddressDto() throws Exception {
//        AddressDto addressDto = new AddressDto();
//        addressDto.setId(1L);
//        addressDto.setCity("city");
//        addressDto.setCountry("country");
//        addressDto.setStreet("street");
//        addressDto.setState("state");
//        addressDto.setZip("12345");
//
//        when(addressService.findById(anyLong())).thenReturn(Optional.of(addressDto));
//
//        mockMvc.perform(get(BASE_URL + "/find-by")
//                        .param("id", "1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.city", is("city")))
//                .andExpect(jsonPath("$.country", is("country")))
//                .andExpect(jsonPath("$.state", is("state")))
//                .andExpect(jsonPath("$.zip", is("12345")))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//    }
//
//    @Test
//    void findById_returnsNone() throws Exception {
//        when(addressService.findById(anyLong())).thenReturn(Optional.empty());
//
//        mockMvc.perform(get(BASE_URL + "/find-by")
//                        .param("id", "1"))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void update_returnUpdatedAddressDto() throws Exception {
//        AddressDto addressDto = new AddressDto();
//        addressDto.setId(1L);
//        addressDto.setCity("city");
//        addressDto.setCountry("country");
//        addressDto.setStreet("street");
//        addressDto.setState("state");
//        addressDto.setZip("12345");
//
//        when(addressService.findById(anyLong()))
//                .thenReturn(Optional.of(addressDto));
//        when(addressService.save(any(AddressDto.class)))
//                .thenReturn(addressDto);
//
//        mockMvc.perform(post(BASE_URL + "/update")
//                        .content(objectMapper.writeValueAsString(addressDto))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.city", is("city")))
//                .andExpect(jsonPath("$.country", is("country")))
//                .andExpect(jsonPath("$.state", is("state")))
//                .andExpect(jsonPath("$.zip", is("12345")))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//    }
//
//    @Test
//    void update_returnNotFound() throws Exception {
//        AddressDto addressDto = new AddressDto();
//        addressDto.setId(1L);
//
//        when(addressService.findById(anyLong()))
//                .thenReturn(Optional.empty());
//
//        mockMvc.perform(post(BASE_URL + "/update")
//                        .content(objectMapper.writeValueAsString(addressDto))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//    }
//}
