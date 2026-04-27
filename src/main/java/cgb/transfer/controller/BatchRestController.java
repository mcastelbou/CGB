package cgb.transfer.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cgb.transfer.dto.BatchRequest;
import cgb.transfer.dto.BatchTransferRequest;
import cgb.transfer.entity.Batch;
import cgb.transfer.exception.BatchException;
import cgb.transfer.exception.CreateTransferException;
import cgb.transfer.service.BatchService;

/**
 * Classe de réception des requêtes HTTP sur la route de gestion des lots de
 * virements.
 */
@RestController
@RequestMapping("/api/batches")
public class BatchRestController {

	/**
	 * Le lien vers le service de gerstion des virements par lots.
	 */
	@Autowired
	private BatchService batchService;

	/**
	 * Méthode POST de gestion des virements par lots.
	 * 
	 * @param batchRequest Le corps de la requête HTTP.
	 * @return Une attestation de bonne réception du lot à gérer, sinon une erreur.
	 */
	@PostMapping
	public ResponseEntity<?> startAsyncTask(@RequestBody BatchRequest batchRequest) {
		try {
			Batch batch = batchService.createBatch(batchRequest);
			List<BatchTransferRequest> transferList = batchRequest.getTransfers();

			batchService.executeBatch(batch.getRefBatch(), transferList);

			batchRequest.setStartDate(batch.getStartDate());
			batchRequest.setStatus("received");

			return ResponseEntity.ok(batchRequest);
		} catch (CreateTransferException e) {
			// Uniquement renvoyée lorsque le lot (hormis la liste de virements) en lui même contient des erreurs.
			TransferResponse errorResponse = new TransferResponse("FAILURE", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
	}
	
	@GetMapping("/{batchId}")
	public ResponseEntity<?> getBatchReport(@RequestParam String batchId) {
		try {
			Batch batch = batchService.findBatch(batchId);
			return ResponseEntity.ok(batch);
		} catch (BatchException e) {
			TransferResponse errorResponse = new TransferResponse("FAILURE", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
	}
}