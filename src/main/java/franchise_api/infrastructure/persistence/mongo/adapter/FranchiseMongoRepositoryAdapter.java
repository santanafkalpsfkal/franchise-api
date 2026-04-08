package franchise_api.infrastructure.persistence.mongo.adapter;

import franchise_api.application.port.out.FranchiseRepository;
import franchise_api.domain.model.Franchise;
import franchise_api.infrastructure.persistence.mongo.mapper.FranchiseDocumentMapper;
import franchise_api.infrastructure.persistence.mongo.repository.SpringDataFranchiseRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class FranchiseMongoRepositoryAdapter implements FranchiseRepository {

	private final SpringDataFranchiseRepository repository;

	public FranchiseMongoRepositoryAdapter(SpringDataFranchiseRepository repository) {
		this.repository = repository;
	}

	@Override
	public Mono<Franchise> save(Franchise franchise) {
		return repository.save(FranchiseDocumentMapper.toDocument(franchise))
				.map(FranchiseDocumentMapper::toDomain);
	}

	@Override
	public Mono<Franchise> findById(String franchiseId) {
		return repository.findById(franchiseId)
				.map(FranchiseDocumentMapper::toDomain);
	}
}