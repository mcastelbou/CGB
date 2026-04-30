package cgb.transfer.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cgb.transfer.dto.BatchRequest;
import cgb.transfer.dto.BatchTransferRequest;
import cgb.transfer.entity.Batch;
import cgb.transfer.entity.BatchTransfer;
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
			// Uniquement renvoyée lorsque le lot (hormis la liste de virements) en lui même
			// contient des erreurs.
			TransferResponse errorResponse = new TransferResponse("FAILURE", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
	}

	/**
	 * Méthode GET de récupération d'un rapport de lot de virement.
	 * 
	 * @param refBatch La référence du lot dont il faut générer le rapport
	 *                 d'execution.
	 * @return Le rapport du lot.
	 */
	@GetMapping("/{refBatch}")
	public ResponseEntity<?> getBatchReport(@PathVariable String refBatch) {
		try {
			Batch batch = batchService.findBatch(refBatch);
			return ResponseEntity.ok(batch);
		} catch (BatchException e) {
			TransferResponse errorResponse = new TransferResponse("FAILURE", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
	}

	/**
	 * Méthode GET de récupération des virements par lots qui ont rencontré des
	 * erreurs pour un lot donné.
	 * 
	 * @param batchRef La référence du lot.
	 * @return La liste des virements en échec.
	 */
	@GetMapping("/transfers/failed/refLot:{batchRef}")
	public ResponseEntity<?> getFailedTransfersWithBatch(@PathVariable String batchRef) {
		try {
			List<BatchTransfer> list = batchService.findFailedTransfersWithBatch(batchRef);
			return ResponseEntity.ok(list);
		} catch (BatchException e) {
			TransferResponse errorResponse = new TransferResponse("FAILURE", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
	}

	/**
	 * Méthode GET de récupération des virements par lots qui ont rencontré des
	 * erreurs pour un compte destinataire donné.
	 * 
	 * @param destAccount L'IBAN du compte destinataire.
	 * @return La liste des virements en échec.
	 */
	@GetMapping("/transfers/failed/destAccount:{destAccount}")
	public ResponseEntity<?> getFailedTransfersWithAccount(@PathVariable String destAccount) {
		List<BatchTransfer> list = batchService.findFailedTransfersWithDestAccount(destAccount);
		return ResponseEntity.ok(list);
	}

	/**
	 * Méthode GET de récupération des virements par lots qui ont rencontré des
	 * erreurs sur un intervalle donné.
	 * 
	 * @param lowLimit  La limite basse de l'intervalle.
	 * @param highLimit La limite haute de l'intervalle.
	 * @return La liste des virements en échec.
	 */
	@GetMapping("/transfers/failed/byDate")
	public ResponseEntity<?> getFailedTransfersWithDate(@RequestParam LocalDate lowLimit,
			@RequestParam LocalDate highLimit) {
		List<BatchTransfer> list = batchService.findFailedTransfersWithDate(lowLimit, highLimit);
		return ResponseEntity.ok(list);
	}
}