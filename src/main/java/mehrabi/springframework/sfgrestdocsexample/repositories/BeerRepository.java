package mehrabi.springframework.sfgrestdocsexample.repositories;

import mehrabi.springframework.sfgrestdocsexample.domain.Beer;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface BeerRepository extends PagingAndSortingRepository<Beer,UUID> {

}
