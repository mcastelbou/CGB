package cgb.transfer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import cgb.transfer.dto.BatchRequest;
import cgb.transfer.dto.BatchTransferRequest;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "user")
public class BatchControllerTest {

	private static BatchTransferRequest mockBTR1;
	private static BatchTransferRequest mockBTR2;
	private static BatchRequest mockBatchRequest;

	@Autowired
	private MockMvc mockMvc;

	@BeforeAll
	static void initObjects() {
		mockBTR1 = new BatchTransferRequest();
		mockBTR1.setAmount(2.00);
		mockBTR1.setDestinationAccount("FR6456670318837484829350380");
		mockBTR1.setDescription("Deuw Euwos !");

		mockBTR2 = new BatchTransferRequest();
		mockBTR2.setAmount(10.00);
		mockBTR2.setDestinationAccount("FR17323632044924987026508039");
		mockBTR2.setDescription("Diz Euwos !");

		ArrayList<BatchTransferRequest> transferList = new ArrayList<BatchTransferRequest>();
		transferList.add(mockBTR1);
		transferList.add(mockBTR2);

		mockBatchRequest = new BatchRequest();
		mockBatchRequest.setSourceAccount("FR7618315100001028575571887");
		mockBatchRequest.setDescription("Lot basique de test.");
		mockBatchRequest.setTransfers(transferList);
	}

	@Test
	void createBatchTest_Success() throws Exception {
		mockMvc.perform(
				post("/api/batches").contentType(MediaType.APPLICATION_JSON).content(asJsonString(mockBatchRequest)))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").exists());
	}
	
	@Test
	void createBatchTest_Failure() throws Exception {
		mockBatchRequest.setSourceAccount("FR7618315100001028575571867");
		
		mockMvc.perform(
				post("/api/batches").contentType(MediaType.APPLICATION_JSON).content(asJsonString(mockBatchRequest)))
				.andExpect(status().isBadRequest());
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().findAndRegisterModules().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
