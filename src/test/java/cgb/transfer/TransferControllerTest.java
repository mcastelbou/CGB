package cgb.transfer;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import cgb.transfer.entity.Transfer;

@SuppressWarnings("unused")
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "user")
public class TransferControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
    private JavaMailSender javaMailSender;

	// Test for successful transfer creation
	@Test
	public void createTransferTest_Success() throws Exception {
		Transfer transfer = new Transfer();
		transfer.setAmount(10.0);
		transfer.setDescription("Test du transfer");
		transfer.setDestinationAccountNumber("FR7618315100001028575571887");
		transfer.setSourceAccountNumber("FR7618315100000406690515531");
		transfer.setTransferDate(LocalDate.now()/* parse("2018-12-06") */);
		mockMvc.perform(post("/api/transfers").contentType(MediaType.APPLICATION_JSON).content(asJsonString(transfer)))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
	}

	@Test
	public void deleteTransferTest_Success() throws Exception {
		Long id = 1L;
		mockMvc.perform(delete("/api/transfers").contentType(MediaType.APPLICATION_JSON).content(String.valueOf(id)))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("SUCCESS"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").exists());
	}

	@Test
	public void createTransferTest_DestinationAccountFailure() throws Exception {
		Transfer transfer = new Transfer();
		transfer.setAmount(2.0);
		transfer.setDescription("Test du transfer");
		transfer.setDestinationAccountNumber("XXXXXXXXXXXXXXXXXXXXXXXXXXX");
		transfer.setSourceAccountNumber("FR7618315100001028575571887");
		transfer.setTransferDate(LocalDate.now());
		mockMvc.perform(post("/api/transfers").content(asJsonString(transfer)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("FAILURE"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("DESTINATION_ACCOUNT_NOT_FOUND"));
	}

	@Test
	public void createTransferTest_IllegalDateFailure() throws Exception {
		Transfer transfer = new Transfer();
		transfer.setAmount(2.0);
		transfer.setDescription("Test du transfer");
		transfer.setDestinationAccountNumber("FR7618315100000406690515531");
		transfer.setSourceAccountNumber("FR7618315100001028575571887");
		transfer.setTransferDate(LocalDate.parse("2001-09-11"));
		mockMvc.perform(post("/api/transfers").content(asJsonString(transfer)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("FAILURE"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("ILLEGAL_DATE"));
	}

	@Test
	public void createTransferTest_InsuficientFundsFailure() throws Exception {
		Transfer transfer = new Transfer();
		transfer.setAmount(400000.0);
		transfer.setDescription("Test du transfer");
		transfer.setDestinationAccountNumber("FR7618315100000406690515531");
		transfer.setSourceAccountNumber("FR7618315100001028575571887");
		transfer.setTransferDate(LocalDate.now());
		mockMvc.perform(post("/api/transfers").content(asJsonString(transfer)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("FAILURE"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("INSUFFICIENT_FUNDS"));
	}

	@Test
	public void createTransferTest_NegativeAmountFailure() throws Exception {
		Transfer transfer = new Transfer();
		transfer.setAmount(-2.0);
		transfer.setDescription("Test du transfer");
		transfer.setDestinationAccountNumber("FR7618315100000406690515531");
		transfer.setSourceAccountNumber("FR7618315100001028575571887");
		transfer.setTransferDate(LocalDate.now());
		mockMvc.perform(post("/api/transfers").content(asJsonString(transfer)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("FAILURE"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("NEGATIVE_AMOUNT"));
	}

	@Test
	public void createTransferTest_SourceAccountFailure() throws Exception {
		Transfer transfer = new Transfer();
		transfer.setAmount(2.0);
		transfer.setDescription("Test du transfer");
		transfer.setDestinationAccountNumber("FR7618315100000406690515531");
		transfer.setSourceAccountNumber("XXXXXXXXXXXXXXXXXXXXXXXXXXX");
		transfer.setTransferDate(LocalDate.now());
		mockMvc.perform(post("/api/transfers").content(asJsonString(transfer)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("FAILURE"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("SOURCE_ACCOUNT_NOT_FOUND"));
	}

	@Test
	void deleteTransferTest_Failure() throws Exception {
		Long id = null;

		mockMvc.perform(delete("/api/transfers").contentType(MediaType.APPLICATION_JSON).content(String.valueOf(id)))
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

//SecurityConfig.java ajouter .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));