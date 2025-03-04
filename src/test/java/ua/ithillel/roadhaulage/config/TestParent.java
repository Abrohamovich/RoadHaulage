package ua.ithillel.roadhaulage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.ithillel.roadhaulage.dto.AuthUserDto;
import ua.ithillel.roadhaulage.filter.JwtAuthenticationFilter;
import ua.ithillel.roadhaulage.service.interfaces.UserService;
import ua.ithillel.roadhaulage.util.JwtUtil;

public abstract class TestParent {
    @Autowired
    protected MockMvc mockMvc;
    @MockitoBean
    protected UserService userService;
    @MockitoBean
    protected JwtUtil jwtUtil;
    @MockitoBean
    protected JwtAuthenticationFilter jwtAuthenticationFilter;

    protected AuthUserDto authUser = new AuthUserDto();
}