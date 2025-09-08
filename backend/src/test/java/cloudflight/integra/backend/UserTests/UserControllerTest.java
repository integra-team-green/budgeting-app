package cloudflight.integra.backend.UserTests;

import cloudflight.integra.backend.dto.UserDto;
import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.repository.UserRepositoryInMemoryImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepositoryInMemoryImpl userRepo;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        userRepo.clear();
        userRepo.save(new User(null, "Alice", "alice@email.com", "123"));
        userRepo.save(new User(null, "Marc", "marc@yahoo.com", "abcd999"));
    }

    @Test
    void testGetUserById() throws Exception {
        mockMvc.perform(get("/users/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@email.com"));
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        mockMvc.perform(get("/users/999"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.name = "Bob";
        userDto.email = "bob@email.com";
        userDto.password = "passw";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.email").value("bob@email.com"));
    }

    @Test
    void testUpdateUser() throws Exception {
        UserDto updateDto = new UserDto();
        updateDto.name = "Alice Updated";
        updateDto.email = "alice@new.com";
        updateDto.password = "newpass";

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice Updated"))
                .andExpect(jsonPath("$.email").value("alice@new.com"));
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/2"))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/users/2"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}