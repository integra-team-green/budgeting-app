package cloudflight.integra.backend.user.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cloudflight.integra.backend.dto.auth.AuthenticationRequest;
import cloudflight.integra.backend.dto.auth.AuthenticationResponse;
import cloudflight.integra.backend.dto.auth.RegisterRequest;
import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class SecurityTests {

    private String testToken;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepo;

    @BeforeEach
    public void setUp() throws Exception {
        userRepo.deleteAll();
        userRepo.save(new User(null, "Alice", "alice@email.com", "123"));
        userRepo.save(new User(null, "Marc", "marc@yahoo.com", "abcd999"));

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("Test User");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        AuthenticationRequest loginRequest = new AuthenticationRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        testToken =
                objectMapper.readValue(response, AuthenticationResponse.class).getToken();
    }

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get("/api/v1/users").header("Authorization", "Bearer " + testToken))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testGetAllWithoutToken() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}
