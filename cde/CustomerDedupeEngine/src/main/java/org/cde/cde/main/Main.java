package org.cde.cde.main;

import java.io.File;
import java.util.List;

import org.cde.cde.service.SearchKeysBuilder;
import org.cde.cde.service.StandardizationService;
import org.cde.cdeio.es.EntitySearchDAO;
import org.cde.cdeio.mongo.EntityDAO;
import org.cde.cdeio.service.SimpleFileLoader;
import org.cde.cdescore.score.ApplyMatrix;
import org.cde.cdescore.score.EntityScoreService;
import org.cde.cdescore.service.EntitySearchService;
import org.cde.commons.factory.SimpleInputEntityFactory;
import org.cde.domain.entity.Entity;

public class Main {

	public static void main(String[] args) {

		if (args.length < 1) {
			System.out.println("Provide entity raw file for process...");
		}

		String inputFilePath = args[0];

		SimpleFileLoader loader = new SimpleFileLoader();
		File inputFile = loader.loadInputFile(inputFilePath);

		SimpleInputEntityFactory factory = new SimpleInputEntityFactory();
		List<Entity> entities = factory.build(inputFile);

		EntityDAO entityDAO = new EntityDAO();
		Long batchId = entityDAO.addEntities(entities);

		StandardizationService service = new StandardizationService();
		service.standardize(entities, batchId);

		SearchKeysBuilder.buildKeys(entities);

		EntitySearchDAO searchDAO = new EntitySearchDAO();
		searchDAO.indexEntities(entities);

		EntitySearchService ess = new EntitySearchService();
		EntityScoreService scoreService = new EntityScoreService();
		ApplyMatrix matrix = new ApplyMatrix();

		for (Entity entity : entities) {
			List<Entity> candidatePool = ess.getCandidatePool(entity);
			scoreService.scoreCandidate(entity, candidatePool);
			List<Entity> acceptedCandidates = matrix.applyAcceptanceMatrix(candidatePool, "MAT-ACT-V001");
			List<Entity> finalCandidates = matrix.applyRejectionMatrix(acceptedCandidates, "MAT-REJ-V001");
			entityDAO.updateEntitiesMatchResult(finalCandidates);
		}

	}

}
