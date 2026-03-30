package cgb.transfer;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

import cgb.transfer.controller.TransferRestController;
import cgb.transfer.entity.Transfer;
import cgb.transfer.service.TransferService;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(TransferRestController.class)
public class TransferControllerUnitTest {
	
	private static Transfer mockTransfer;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	private TransferService transferService;
	
	@BeforeAll
	static void initObjects() {
		mockTransfer = new Transfer();
		mockTransfer.setId(1L);
		mockTransfer.setSourceAccountNumber("123456789");
		mockTransfer.setDestinationAccountNumber("567891234");
		mockTransfer.setAmount(2.00);
		mockTransfer.setTransferDate(LocalDate.now());
		mockTransfer.setDescription("Deux euros!");
	}
	
	@Test
	void shouldCreateTransfer_Success() throws Exception {
		when(transferService.createTransfer(
				Mockito.any(String.class),
				Mockito.any(String.class),
				Mockito.any(Double.class),
				Mockito.any(LocalDate.class),
				Mockito.any(String.class))).thenReturn(mockTransfer);
		
		mockMvc.perform(post("/api/transfers").contentType(MediaType.APPLICATION_JSON).content(asJsonString(mockTransfer)))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
	}
	
	@Test
	void shouldDeleteTransfer_Success() throws Exception {
		Long id = 1L;
		when(transferService.deleteTransfer(Mockito.anyLong())).thenReturn(mockTransfer);
		
		mockMvc.perform(delete("/api/transfers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(String.valueOf(id)))
		.andExpect(status().isOk())
		.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("SUCCESS"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.message").exists());
	}
	
	@Test
	void shouldCreateTransfer_Failure() throws Exception {
		/*when(transferService.createTransfer(
				Mockito.any(String.class),
				Mockito.any(String.class),
				Mockito.any(Double.class),
				Mockito.any(LocalDate.class),
				Mockito.any(String.class))).thenReturn(mockTransfer);*/
		
		mockMvc.perform(post("/api/transfers").contentType(MediaType.APPLICATION_JSON).content(asJsonString(null)))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	void shouldDeleteTransfer_Failure() throws Exception {
		Long id = null;
		//when(transferService.deleteTransfer(Mockito.anyLong())).thenReturn(mockTransfer);
		
		mockMvc.perform(delete("/api/transfers")
				.contentType(MediaType.APPLICATION_JSON)
	            .content(String.valueOf(id)))
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
