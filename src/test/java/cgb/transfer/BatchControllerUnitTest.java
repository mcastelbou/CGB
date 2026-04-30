package cgb.transfer;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import cgb.transfer.controller.BatchRestController;
import cgb.transfer.dto.BatchRequest;
import cgb.transfer.entity.Batch;
import cgb.transfer.entity.BatchTransfer;
import cgb.transfer.service.BatchService;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(BatchRestController.class)
public class BatchControllerUnitTest {

	private static BatchTransfer mockBatchTransfer;
	private static Batch mockBatch;
	private static BatchRequest mockBatchRequest;

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private BatchService batchService;
	
	@BeforeAll
	static void initObjects() {
		mockBatchRequest = new BatchRequest();
		mockBatchRequest.setRefBatch(LocalDate.now() + "-1");
		mockBatchRequest.setSourceAccount("FR7618315100001028575571887");
		mockBatchRequest.setDescription("Lot basique");
		
		mockBatchTransfer = new BatchTransfer();
		mockBatchTransfer.setId(1L);
		mockBatchTransfer.setAmount(2.00);
		mockBatchTransfer.setCompletionDate();
		mockBatchTransfer.setDescription("Deux euros !");
		mockBatchTransfer.setDestinationAccount("FR7618315100000406690515531");
		mockBatchTransfer.setStatus("delayed");
		mockBatchTransfer.setBatch(mockBatch);
		
		List<BatchTransfer> transferList = new ArrayList<BatchTransfer>();
		transferList.add(mockBatchTransfer);
		
		mockBatch = new Batch();
		mockBatch.setRefBatch(LocalDate.now() + "-1");
		mockBatch.setSourceAccount("FR7618315100001028575571887");
		mockBatch.setStartDate(LocalDate.now());
		mockBatch.setDescription("Lot basique");
		mockBatch.setStatus("received");
		mockBatch.setTransferList(transferList);
	}

	@Test
	void shouldReturnBatch_Success() throws Exception {
		when(batchService.createBatch(Mockito.any(BatchRequest.class))).thenReturn(mockBatch);

		mockMvc.perform(
				post("/api/batches").contentType(MediaType.APPLICATION_JSON).content(asJsonString(mockBatchRequest)))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$.startDate").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").exists());
	}

	/*@Test
	void shouldReturnBatch_Failure() throws Exception {
		mockBatchRequest.setSourceAccount("XXXXXXXXXXXXXXXXXXXXXXXXXXX");

		mockMvc.perform(
				post("/api/batches").contentType(MediaType.APPLICATION_JSON).content(asJsonString(mockBatchRequest)))
				.andExpect(status().isBadRequest());
	}*/
	
	@Test
	void shouldReturnBatchReport_Success() throws Exception {
		when(batchService.findBatch(Mockito.any(String.class))).thenReturn(mockBatch);
		
		mockMvc.perform(
				get("/api/batches/" + LocalDate.now() + "-1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
	}
	
	/*@Test
	void shouldReturnBatchReport_Failure() throws Exception {
		mockMvc.perform(
				get("/api/batches/" + null).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}*/

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().findAndRegisterModules().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
