package io.github.serafimkogan.hebrewutils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class NikkudimSpreaderTest {
    @Test
    public void shouldThrowNullPointerException1() {
		String reference = null;
		String billet = "תאווה";
		assertThrows(NullPointerException.class, () -> new NikkudimSpreader(reference, billet));
    }

    @Test
    public void shouldThrowNullPointerException2() {
		String reference = "טַיֶּסֶתָהּ";
		String billet = null;
		assertThrows(NullPointerException.class, () -> new NikkudimSpreader(reference, billet));
    }
    
    @Test
    public void shouldThrowNullPointerException3() {
		String reference = null;
		String billet = null;
		assertThrows(NullPointerException.class, () -> new NikkudimSpreader(reference, billet));
    }
	
    @Test
    public void shouldThrowIllegalArgumentException1() {
		String reference = "טַיֶּסֶתָהּ";
		String billet = "תאווה";
		assertThrows(IllegalArgumentException.class, () -> new NikkudimSpreader(reference, billet));
    }
    
    @Test
    public void shouldThrowIllegalArgumentException2() {
		String reference = "רֵאַת";
		String billet = "שימור";
		assertThrows(IllegalArgumentException.class, () -> new NikkudimSpreader(reference, billet));
    }
    
    @Test
    public void shouldThrowIllegalArgumentException3() {
		String reference = "קְנִיָּה";
		String billet = "צוותי";
		assertThrows(IllegalArgumentException.class, () -> new NikkudimSpreader(reference, billet));
    }
    
    @Test
    public void shouldThrowIllegalArgumentException4() {
		String reference = "עֲשֶׂרֶת הַדִּבְּרוֹת";
		String billet = "עקידה";
		assertThrows(IllegalArgumentException.class, () -> new NikkudimSpreader(reference, billet));
    }
    
    @Test
    public void shouldThrowIllegalArgumentException5() {
		String reference = "עֹרְבִים";
		String billet = "סכמות";
		assertThrows(IllegalArgumentException.class, () -> new NikkudimSpreader(reference, billet));
    }
	
	@Test
	public void shouldSpreadNikkudimCorrectly1() {
		String reference = "טַיֶּסֶתָהּ";
		String billet = "טייסתה";
		String expected = "טַייֶּסֶתָהּ";
		NikkudimSpreader spreader = new NikkudimSpreader(reference, billet);
		String actual = spreader.getResult();
		assertEquals(expected, actual);
	}

	@Test
	public void shouldSpreadNikkudimCorrectly2() {
		String reference = "רִגּוּל תַּעֲשִׂיָּתִי";
		String billet = "ריגול תעשייתי";
		String expected = "ריגּוּל תַּעֲשִׂייָּתִי";
		NikkudimSpreader spreader = new NikkudimSpreader(reference, billet);
		String actual = spreader.getResult();
		System.out.println(spreader.toString());
		assertEquals(expected, actual);
	}

	@Test
	public void shouldSpreadNikkudimCorrectly3() {
		String reference = "שַׁמְרָנַי";
		String billet = "שמרניי";
		String expected = "שַׁמְרָנַיי";
		NikkudimSpreader spreader = new NikkudimSpreader(reference, billet);
		String actual = spreader.getResult();
		assertEquals(expected, actual);
	}

	@Test
	public void shouldSpreadNikkudimCorrectly4() {
		String reference = "אָזְנֵי";
		String billet = "אוזני";
		String expected = "אוֹזְנֵי";
		NikkudimSpreader spreader = new NikkudimSpreader(reference, billet);
		String actual = spreader.getResult();
		assertEquals(expected, actual);
	}

	@Test
	public void shouldSpreadNikkudimCorrectly5() {
		String reference = "תִּסְמֹנֶת הָאַף הַלָּבָן";
		String billet = "תסמונת האף הלבן";
		String expected = "תִּסְמוֹנֶת הָאַף הַלָּבָן";
		NikkudimSpreader spreader = new NikkudimSpreader(reference, billet);
		String actual = spreader.getResult();
		assertEquals(expected, actual);
	}

	@Test
	public void shouldSpreadNikkudimCorrectly6() {
		String reference = "אֲסֵפַתְכֶם";
		String billet = "אסיפתכם";
		String expected = "אֲסיפַתְכֶם";
		NikkudimSpreader spreader = new NikkudimSpreader(reference, billet);
		String actual = spreader.getResult();
		assertEquals(expected, actual);
	}
	
	@Test
	public void shouldSpreadNikkudimCorrectly7() {
		String reference = "אַוָּזֵי";
		String billet = "אווזי";
		String expected = "אַווָּזֵי";
		NikkudimSpreader spreader = new NikkudimSpreader(reference, billet);
		String actual = spreader.getResult();
		assertEquals(expected, actual);
	}

	@Test
	public void shouldSpreadNikkudimCorrectly8() {
		String reference = "אֲוִירוֹת";
		String billet = "אווירות";
		String expected = "אֲווִירוֹת";
		NikkudimSpreader spreader = new NikkudimSpreader(reference, billet);
		String actual = spreader.getResult();
		assertEquals(expected, actual);
	}

	@Test
	public void shouldSpreadNikkudimCorrectly9() {
		String reference = "הִתְיַקְּרֻיּוֹת";
		String billet = "התייקרויות";
		String expected = "הִתְייַקְּרוֹיּוֹת";
		NikkudimSpreader spreader = new NikkudimSpreader(reference, billet);
		String actual = spreader.getResult();
		assertEquals(expected, actual);
	}


	@Test
	public void shouldSpreadNikkudimCorrectly10() {
		String reference = "זָוִיּוֹת";
		String billet = "זוויות";
		String expected = "זָווִיּוֹת";
		NikkudimSpreader spreader = new NikkudimSpreader(reference, billet);
		String actual = spreader.getResult();
		assertEquals(expected, actual);
	}

	@Test
	public void shouldSpreadNikkudimCorrectly11() {
		String reference = "חִוּוּט";
		String billet = "חיווט";
		String expected = "חִיווּט";
		NikkudimSpreader spreader = new NikkudimSpreader(reference, billet);
		String actual = spreader.getResult();
		assertEquals(expected, actual);
	}

	@Test
	public void shouldSpreadNikkudimCorrectly12() {
		String reference = "סִיֹּמֶת";
		String billet = "סיומת";
		String expected = "סִיוֹמֶת";
		NikkudimSpreader spreader = new NikkudimSpreader(reference, billet);
		String actual = spreader.getResult();
		assertEquals(expected, actual);
	}
}
