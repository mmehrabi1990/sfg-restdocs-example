package mehrabi.springframework.sfgrestdocsexample.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mehrabi.springframework.sfgrestdocsexample.domain.Beer;
import mehrabi.springframework.sfgrestdocsexample.repositories.BeerRepository;
import mehrabi.springframework.sfgrestdocsexample.web.model.BeerDto;
import mehrabi.springframework.sfgrestdocsexample.web.model.BeerStyleEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(uriScheme = "http",uriHost = "localhost",uriPort = 8080)
@WebMvcTest
@ComponentScan(basePackages = "mehrabi.springframework.sfgrestdocsexample.web.mappers")
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BeerRepository beerRepository;

    @Test
    void getBeerById() throws Exception {
        given(beerRepository.findById(any())).willReturn(Optional.of(Beer.builder().build()));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/beer/{beerId}", UUID.randomUUID().toString())
                .param("isCold","yes")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(document("v1/beer",
                pathParameters(parameterWithName("beerId").description("UUID of desired beer to get."))
                ,requestParameters(parameterWithName("isCold").description("Is beer cold Query param"))
                ,responseFields(
                        fieldWithPath("id").description("Id of Beer"),
                        fieldWithPath("version").description("Version number"),
                        fieldWithPath("createdDate").description("Date Created"),
                        fieldWithPath("lastModifiedDate").description("Date Updated"),
                        fieldWithPath("beerName").description("Beer Name"),
                        fieldWithPath("beerStyle").description("Beer Style"),
                        fieldWithPath("upc").description("UPS of Beer"),
                        fieldWithPath("price").description("Price"),
                        fieldWithPath("quantityOnHand").description("Quantity On Hand")
                )));
    }

    @Test
    void saveNewBeer() throws Exception {
        BeerDto beerDto = getValidBeerDto();
        String value = objectMapper.writeValueAsString(beerDto);

        ConstrainedFields fields = new ConstrainedFields(BeerDto.class);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/beer/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(value)).andExpect(status().isCreated()).andDo(document("v1/beer",requestFields(
                        fields.withPath("id").ignored(),
                        fields.withPath("version").ignored(),
                        fields.withPath("createdDate").ignored(),
                        fields.withPath("lastModifiedDate").ignored(),
                        fields.withPath("beerName").description("Name of the Beer"),
                        fields.withPath("beerStyle").description("Name of Beer"),
                        fields.withPath("upc").description("Beer UPC"),
                        fields.withPath("price").description("Beer Price"),
                        fields.withPath("quantityOnHand").ignored()
                )));
    }

    @Test
    void updateBeerById() throws Exception {
        BeerDto beerDto = getValidBeerDto();
        String value = objectMapper.writeValueAsString(beerDto);
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/v1/beer/"+ UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(value)).andExpect(status().isNoContent());

    }

    BeerDto getValidBeerDto(){
        return BeerDto.builder()
                .beerName("My Beer")
                .beerStyle(BeerStyleEnum.ALE)
                .price(new BigDecimal("2.99"))
                .upc(123123123123L)
                .build();
    }

    private static class ConstrainedFields{

        private final ConstraintDescriptions constraintDescriptions;

         ConstrainedFields(Class<?> input){
             this.constraintDescriptions = new ConstraintDescriptions(input);
         }

         private FieldDescriptor withPath(String path){
            return fieldWithPath(path).attributes(key("constraints").value(StringUtils
            .collectionToDelimitedString(this.constraintDescriptions.descriptionsForProperty(path),".")));
         }
    }
}