package cl.wom.api.persistence;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.MDC;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import cl.wom.api.persistence.repository.PersonRepository;
import cl.wom.api.persistence.to.PersonDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MongoDAO {

	private PersonRepository personRepository;

	private MongoTemplate mongoTemplate;

	public MongoDAO(PersonRepository personRepository, MongoTemplate mongoTemplate) {
		this.personRepository = personRepository;
		this.mongoTemplate = mongoTemplate;
	}

	public void methodWithRepository() {
		log.info("[methodWithRepository] Begin");

		PersonDTO person = buildPerson();

		// Default method for save (upsert)
		personRepository.save(person);

		// Custom search
		PersonDTO findByRutPerson = personRepository.findByRut(person.getRut());
		log.info("[methodWithRepository] findByRutPerson={}", findByRutPerson);

		// Custom search several fields
		PersonDTO findByRutAndActiveAndFriendsInPerson = personRepository
				.findByRutAndActiveAndFriendsIn(person.getRut(), true, "Juan");
		log.info("[methodWithRepository] findByRutAndActiveAndFriendsInPerson={}",
				findByRutAndActiveAndFriendsInPerson);

		// Custom search by query
		List<PersonDTO> findByActiveAndNamePerson = personRepository.findByActiveAndName(true, "Pedro");
		log.info("[methodWithRepository] findByActiveAndNamePerson {}", findByActiveAndNamePerson);

		personRepository.delete(person);
		log.info("[methodWithRepository] delete={}", person);

		log.info("[methodWithRepository] End");

	}

	public void methodWithMongoTemplate(String name, Date dateLte, Date dateGt, Boolean active) {
		log.info("[methodWithMongoTemplate] Begin");

		PersonDTO person = buildPerson();

		// Upsert
		mongoTemplate.save(person);
		log.info("[methodWithMongoTemplate] save={}", person);

		PersonDTO findByIdPerson = mongoTemplate.findById(person.getId(), PersonDTO.class);
		log.info("[methodWithMongoTemplate] findByIdPerson={}", findByIdPerson);

		// Query example
		Query query = new Query();

		if (name != null && !name.trim().equals("")) {
			query.addCriteria(Criteria.where("name").regex(".*" + name + ".*", "i"));
		}

		if (dateLte != null) {
			query.addCriteria(Criteria.where("date").gte(dateLte));
		}

		if (dateGt != null) {
			query.addCriteria(Criteria.where("date").lt(dateGt));
		}

		if (active != null) {
			query.addCriteria(Criteria.where("active").is(active));
		}

		// Find one by query
		PersonDTO findOnePerson = mongoTemplate.findOne(query, PersonDTO.class);
		log.info("[methodWithMongoTemplate] findOnePerson={}", findOnePerson);

		// Find many by query
		List<PersonDTO> findPeople = mongoTemplate.find(query, PersonDTO.class);
		log.info("[methodWithMongoTemplate] findPeople={}", findPeople);

		// Delete
		mongoTemplate.remove(person);
		log.info("[methodWithMongoTemplate] remove={}", person);

		log.info("[methodWithMongoTemplate] End");

	}

	private PersonDTO buildPerson() {
		PersonDTO person = new PersonDTO();
		person.setRut(MDC.get("trace-id"));
		person.setName("Pedro");
		person.setActive(true);
		person.setFriends(Arrays.asList(new String[] { "Juan", "Diego" }));
		return person;
	}
}
