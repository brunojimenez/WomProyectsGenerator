package cl.wom.batch.persistence.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import cl.wom.batch.persistence.to.PersonDTO;

@Repository
public interface PersonRepository extends MongoRepository<PersonDTO, String> {

	// Custom search
	PersonDTO findByRut(String rut);

	// Custom search
	PersonDTO findByRutAndActiveAndFriendsIn(String rut, boolean active, String friends);

	// Custom search list by query
	@Query(value = "{ 'active':?0, 'name':?1 }")
	List<PersonDTO> findByActiveAndName(boolean active, String name);

}
