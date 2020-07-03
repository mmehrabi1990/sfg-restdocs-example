package mehrabi.springframework.sfgrestdocsexample.web.mappers;

import mehrabi.springframework.sfgrestdocsexample.domain.Beer;
import mehrabi.springframework.sfgrestdocsexample.web.model.BeerDto;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class})
public interface BeerMapper {
    BeerDto beerToBeerDTO(Beer beer);

    Beer beerDTOToBeer(BeerDto beerDTO);
}
