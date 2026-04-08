package franchise_api.application.port.out;

import franchise_api.domain.model.Franchise;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseRepository {

	Flux<Franchise> findAll();

	Mono<Franchise> save(Franchise franchise);

	Mono<Franchise> findById(String franchiseId);
}