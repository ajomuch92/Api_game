package com.agile.api.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.agile.api.game.ApiGameApplication;
import com.agile.api.game.Categoria;
import com.agile.api.game.CategoriaRepository;
import com.agile.api.game.Usuario;
import com.agile.api.game.UsuarioRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiGameApplication.class)
@WebAppConfiguration
public class TestApiGame {
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;
    private Usuario user1;
    private Usuario user2;

    private List<Categoria> listaCategorias = new ArrayList<>();
    
    @Autowired
    CategoriaRepository categoriaRepository;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
            .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
            .findAny()
            .orElse(null);

        assertNotNull("the JSON message converter must not be null",this.mappingJackson2HttpMessageConverter);
    }
    
    @SuppressWarnings("unchecked")
	protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.categoriaRepository.deleteAll();
        this.usuarioRepository.deleteAll();
        Categoria k=new Categoria();
        k.setNombre("Forward");
        Set<Usuario> usuarios=new HashSet<>();
        this.user1=new Usuario("Aarón Montes",99,99980,k);
        this.user2=new Usuario("Edie Flores",123,12345,k);
        usuarios.add(this.user1);
        usuarios.add(this.user2);
        k.setUsuarios(usuarios);
        listaCategorias.add(k);
        Categoria cat=categoriaRepository.save(k);
        cat.getUsuarios().forEach(usuarioRepository::save);
    }
    
    @Test
    public void categoriaNotFound() throws Exception {
        mockMvc.perform(post("/categoria")
                .content(this.json(new Categoria("Overall")))
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void readSingleCategory() throws Exception {
        mockMvc.perform(get("/categorias/" + this.listaCategorias.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(this.listaCategorias.get(0).getId())))
                .andExpect(jsonPath("$.nombre", is(this.listaCategorias.get(0).getNombre())));
    }
    
    @Test
    public void readSingleCategoryPlayert() throws Exception{
    	mockMvc.perform(get("/categorias/" + this.listaCategorias.get(0).getId()+"/jugadores"))
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(contentType));
    }
    
    @Test
    public void readOnePlayer() throws Exception{
    	mockMvc.perform(get("/jugadores/" + this.user1.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.id", is(this.user1.getId())))
        .andExpect(jsonPath("$.nombre", is(this.user1.getNombre())));
    }
    
    @Test
    public void readWrongPlayer() throws Exception{
    	mockMvc.perform(get("/jugadores/1234213"))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(contentType));
    }
}
