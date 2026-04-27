package cgb.transfer;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

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
import cgb.transfer.service.BatchService;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(BatchRestController.class)
public class BatchControllerUnitTest {

	private static Batch mockBatch;
	private static BatchRequest mockBatchRequest;

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private BatchService batchService;

	@BeforeAll
	static void initObjects() {
		mockBatch = new Batch();
		mockBatch.setRefBatch(LocalDate.now() + "-1");
		mockBatch.setSourceAccount("FR7618315100001028575571887");
		mockBatch.setStartDate(LocalDate.now());
		mockBatch.setDescription("Lot basique");
		mockBatch.setStatus("received");

		mockBatchRequest = new BatchRequest();
		mockBatchRequest.setRefBatch(LocalDate.now() + "-1");
		mockBatchRequest.setSourceAccount("FR7618315100001028575571887");
		mockBatchRequest.setDescription("Lot basique");
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
		mockBatchRequest.setSourceAccount(null);

		mockMvc.perform(
				post("/api/batches").contentType(MediaType.APPLICATION_JSON).content(asJsonString(mockBatchRequest)))
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
