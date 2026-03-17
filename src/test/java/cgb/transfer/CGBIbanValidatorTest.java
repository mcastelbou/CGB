package cgb.transfer;

import cgb.transfer.exception.InvalidIbanFormatException;
import cgb.utils.CGBIbanValidator;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class CGBIbanValidatorTest {

	private static CGBIbanValidator validator;

	@BeforeAll
	public static void setupBeforeClass() {
		validator = CGBIbanValidator.getIntanceValidator();
	}

	@Test
	void testGetInstanceValidator() {
		assertNotNull(validator);
		assertTrue(validator instanceof CGBIbanValidator);
	}

	@Test
	void testIsIbanStructureValide() {
		try {
			assertTrue(validator.isIbanStructureValide("FR0123456789012345678901234"));
			assertTrue(validator.isIbanStructureValide("FR7618315100001028575571887"));
			assertFalse(validator.isIbanStructureValide("FR012345678901234567890123"));
			assertFalse(validator.isIbanStructureValide("test"));
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}
	}

	@Test
	void testIsIbanValide() {
		try {
			assertTrue(validator.isIbanValide("FR7618315100001028575571887"));
			assertFalse(validator.isIbanValide("FR2643353179133283255475774"));
			assertFalse(validator.isIbanValide("FR0123456789012345678901234"));
			assertThrows(InvalidIbanFormatException.class, () -> validator.isIbanValide("FR012345678901234567890123"));
			assertThrows(InvalidIbanFormatException.class, () -> validator.isIbanValide("FR01234567B90123A56789O1234"));
			assertThrows(InvalidIbanFormatException.class, () -> validator.isIbanValide("FR0000000000000000000000000"));
			assertThrows(InvalidIbanFormatException.class, () -> validator.isIbanValide("test"));
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}
	}

	@Test
	void testGetCodePays() {
		try {
			assertEquals(validator.getCodePays("FR7618315100001028575571887"), "FR");
			assertThrows(InvalidIbanFormatException.class,
					() -> validator.getCodePays("KW90VHWE8113451698654999676337"));
			assertThrows(InvalidIbanFormatException.class, () -> validator.getCodePays("test"));
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}
	}

	@Test
	void testGetCRC() {
		try {
			assertEquals(validator.getCRC("FR7618315100001028575571887"), "76");
			assertEquals(validator.getCRC("FR0123456789012345678901234"), "01");
			// assertEquals(validator.getCRC("test"), "st")
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}
	}

	@Test
	void testGetBBAN() {
		try {
			assertEquals(validator.getBBAN("FR7618315100001028575571887"), "18315100001028575571887");
			assertEquals(validator.getBBAN("FR0123456789012345678901234"), "23456789012345678901234");
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}
	}
}
