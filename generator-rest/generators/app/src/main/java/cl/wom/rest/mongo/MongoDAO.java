package cl.wom.rest.mongo;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import cl.wom.rest.mongo.dto.PersonDTO;
import cl.wom.rest.mongo.repository.PersonRepository;
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

		PersonDTO person = buildPerson();

		// Default method for save (upsert)
		personRepository.save(person);

		// Custom search
		personRepository.findByRut("123");

		// Custom search several fields
		personRepository.findByRutAndActiveAndFriendsIn("123", true, "Juan,Diego");

		// Custom search by query
		personRepository.customFindById(person.getId());

		// Custom search by query
		List<PersonDTO> people = personRepository.findByActiveAndName(true, "Pedro");

		log.debug("People {}", people);

	}

	public void methodWithMongoTemplate() {

		PersonDTO person = buildPerson();

		mongoTemplate.save(person);

		mongoTemplate.remove(person);

	}

	public void methodWithMongoTemplateQuery(String name, Date dateLte, Date dateGt, Boolean active) {

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

		// Find one

		PersonDTO person = mongoTemplate.findOne(query, PersonDTO.class);

		log.debug("Person {}", person);

		// Find many

		List<PersonDTO> people = mongoTemplate.find(query, PersonDTO.class);

		log.debug("People {}", people);

	}

	private PersonDTO buildPerson() {
		PersonDTO person = new PersonDTO();
		person.setRut("123");
		person.setName("Pedro");
		person.setActive(true);
		person.setFriends(Arrays.asList(new String[] { "Juan", "Diego" }));
		return person;
	}
}
