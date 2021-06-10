package com.bezkoder.springjwt.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.bezkoder.springjwt.models.Item;
import com.bezkoder.springjwt.repository.ItemRepository;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.bezkoder.springjwt.exception.ResourceNotFoundException;

import com.bezkoder.springjwt.security.jwt.AuthEntryPointJwt;
import com.bezkoder.springjwt.security.jwt.JwtUtils;
import com.bezkoder.springjwt.security.services.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class ItemControllerTest {
    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private AuthEntryPointJwt unauthorizedHandler;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private ItemRepository itemRepository;

    Item item1 =  null;
    Item item2 = null;
    List<Item> list = null;
    @Autowired
    private MockMvc mockMvc;
    @Before
    public void setup(){
        item1 = new Item(1 , "item1" , "type1 " , 1000 , 1);
        item2 = new Item(2 , "item2" , "type2 " , 2000 , 1);

        list = Arrays.asList(item1, item2);
    }
    // lấy về tất cả Item
    @Test
    @WithMockUser(username = "tester", roles = { "ADMIN" })
    public void getAllItem_test() throws Exception {

        doReturn(list).when(itemRepository).findAll();

        mockMvc.perform(get("/api/v1/items"))

                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("item1")));
    }

// Lay ra 1 item theo id
    @Test
    @WithMockUser(username = "tester", roles = { "ADMIN" })
    public void getItem_test() throws Exception{

        doReturn(Optional.of(item1)).when(itemRepository).findById(1);


        mockMvc.perform(get("/api/v1/items/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
               // .andExpect(content().contentType("application/json"));

    }
    // Lay ra 1 item theo id , truong hop khong ton tai
    @Test
    @WithMockUser(username = "tester", roles = { "ADMIN" })
    public void getItem_test_notExist() throws Exception{

        doReturn(Optional.empty()).when(itemRepository).findById(30);


        mockMvc.perform(get("/api/v1/items/30")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
    // tạo mới Item
    @Test
    @WithMockUser(username = "tester", roles = { "ADMIN" })
    public void createItem_test() throws Exception {
        Item item3 = new Item(3 , "item3" , "type3 " , 1000 , 1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(item3);

        mockMvc.perform(post("/api/v1/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk());
    }
    // tạo mới Item nhưng đã tồn tại
    @Test
    @WithMockUser(username = "tester", roles = { "ADMIN" })
    public void createItem_test_existing() throws Exception {
        Item item3 = new Item(2 , "item2" , "type2 " , 2000 , 1);
        doReturn(item3).when(itemRepository).save(item3);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(item3);

        mockMvc.perform(post("/api/v1/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    // Update Item
    @Test
    @WithMockUser(username = "tester", roles = { "ADMIN" })
    public void updateItem_test() throws Exception {
        String name = "UpdatedItemName";
        Integer id = 1;
        Item item2 = new Item(1 , "item3" , "type3 " , 1000 , 1);


        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

        // Need to create mock data for repository layer
        when(itemRepository.findById(id)).thenReturn(Optional.of(item2));
        item2.setName(name);
        when(itemRepository.save(item2)).thenReturn(item2);

        String requestJson = ow.writeValueAsString(item2);

        mockMvc.perform(put("/api/v1/items/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk());

        assertTrue(item2.getName().equals(name));
    }
    // Update Item loi do khong ton tai
    @Test
    @WithMockUser(username = "tester", roles = { "ADMIN" })
    public void updateItem_test_notExist() throws Exception {
        Item item2 = new Item(20 , "item3" , "type3 " , 1000 , 1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(item2);

        mockMvc.perform(put("/api/v1/items/{itemId}",20)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isNotFound());
                //.andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
               // .andExpect(
                      //  result -> assertEquals("ItemId 20 not found", result.getResolvedException().getMessage()));;
    }

    // xóa Item
    @Test
    @WithMockUser(username = "tester", roles = { "ADMIN" })
    public void deleteItem_test() throws Exception {
        Item item2 = new Item(2 , "item2" , "type2 " , 2000 , 1);

        doReturn(Optional.of(item2)).when(itemRepository).findById(item2.getId());

        mockMvc.perform(delete("/api/v1/items/{itemId}",2))
                .andExpect(status().is2xxSuccessful());
    }

    // xóa Item thất bại, không tìm thấy Item
    @Test
    @WithMockUser(username = "tester", roles = { "ADMIN" })
    public void deleteItem_test_notExist() throws Exception {


        doReturn(Optional.empty()).when(itemRepository).findById(20);

        mockMvc.perform(delete("/api/v1/items/{itemId}",20))

                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(
                        result -> assertEquals("ItemId 20 not found", result.getResolvedException().getMessage()));
    }
}