package cl.wom.batch.step.one;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import cl.wom.batch.step.one.to.StepOneDataTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class StepOneReader {

	private final MongoTemplate mongoTemplate;

	public ItemReader<? extends StepOneDataTO> reader() {

		log.info("[reader] Beggin");

		Criteria criteria = Criteria.where("status").is("OK");

		Query query = new Query();
		query.addCriteria(criteria);

		MongoItemReader<StepOneDataTO> mongoItemreader = new MongoItemReader<>();
		mongoItemreader.setTemplate(mongoTemplate);
		mongoItemreader.setCollection("batch");
		mongoItemreader.setQuery(query);
		mongoItemreader.setPageSize(100);
		mongoItemreader.setTargetType(StepOneDataTO.class);

		log.info("[reader] End");

		return mongoItemreader;
	}
}
