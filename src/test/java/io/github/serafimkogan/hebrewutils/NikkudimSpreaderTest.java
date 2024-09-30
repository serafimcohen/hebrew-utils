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
	public void shouldBeTrue1() {
		String reference = "טַיֶּסֶתָהּ";
		String billet = "טייסתה";
		String expectedResult = "טַייֶּסֶתָהּ";
		NikkudimSpreader spreader = new NikkudimSpreader(reference, billet);
		String realResult = spreader.getResult();
		assertEquals(expectedResult, realResult);
	};

	@Test
	public void shouldBeTrue2() {
		String reference = "טַיֶּסֶתָן";
		String billet = "טייסתן";
		String expectedResult = "טַייֶּסֶתָן";
		NikkudimSpreader spreader = new NikkudimSpreader(reference, billet);
		String realResult = spreader.getResult();
		assertEquals(expectedResult, realResult);
	};

	@Test
	public void shouldBeTrue3() {
		String reference = "רִגּוּל תַּעֲשִׂיָּתִי";
		String billet = "ריגול תעשייתי";
		String expectedResult = "ריגּוּל תַּעֲשִׂייָּתִי";
		NikkudimSpreader spreader = new NikkudimSpreader(reference, billet);
		String realResult = spreader.getResult();
		assertEquals(expectedResult, realResult);
	};

	@Test
	public void shouldBeTrue4() {
		String reference = "שַׁמְרָנַי";
		String billet = "שמרניי";
		String expectedResult = "שַׁמְרָנַיי";
		NikkudimSpreader spreader = new NikkudimSpreader(reference, billet);
		String realResult = spreader.getResult();
		assertEquals(expectedResult, realResult);
	};

	@Test
	public void shouldBeTrue5() {
		String reference = "אֲבוֹתַי";
		String billet = "אבותיי";
		String expectedResult = "אֲבוֹתַיי";
		NikkudimSpreader spreader = new NikkudimSpreader(reference, billet);
		String realResult = spreader.getResult();
		assertEquals(expectedResult, realResult);
	};

	@Test
	public void shouldBeTrue6() {
		String reference = "אָזְנֵי";
		String billet = "אוזני";
		String expectedResult = "אוֹזְנֵי";
		NikkudimSpreader spreader = new NikkudimSpreader(reference, billet);
		String realResult = spreader.getResult();
		assertEquals(expectedResult, realResult);
	};

	@Test
	public void shouldBeTrue7() {
		String reference = "אֵיבוֹתַי";
		String billet = "איבותיי";
		String expectedResult = "אֵיבוֹתַיי";
		NikkudimSpreader spreader = new NikkudimSpreader(reference, billet);
		String realResult = spreader.getResult();
		assertEquals(expectedResult, realResult);
	};

	@Test
	public void shouldBeTrue8() {
		String reference = "תִּסְמֹנֶת הָאַף הַלָּבָן";
		String billet = "תסמונת האף הלבן";
		String expectedResult = "תִּסְמוֹנֶת הָאַף הַלָּבָן";
		NikkudimSpreader spreader = new NikkudimSpreader(reference, billet);
		String realResult = spreader.getResult();
		assertEquals(expectedResult, realResult);
	};

	@Test
	public void shouldBeTrue9() {
		String reference = "בִּכּוּרַיִךְ";
		String billet = "בכורייך";
		String expectedResult = "בִּכּוּרַייִךְ";
		NikkudimSpreader spreader = new NikkudimSpreader(reference, billet);
		String realResult = spreader.getResult();
		assertEquals(expectedResult, realResult);
	};

	@Test
	public void shouldBeTrue10() {
		String reference = "גְּבִינוֹתַי";
		String billet = "גבינותיי";
		String expectedResult = "גְּבִינוֹתַיי";
		NikkudimSpreader spreader = new NikkudimSpreader(reference, billet);
		String realResult = spreader.getResult();
		assertEquals(expectedResult, realResult);
	};


	@Test
	public void shouldBeTrue11() {
		String reference = "אֲסֵפַתְכֶם";
		String billet = "אסיפתכם";
		String expectedResult = "אֲסיפַתְכֶם";
		NikkudimSpreader spreader = new NikkudimSpreader(reference, billet);
		String realResult = spreader.getResult();
		assertEquals(expectedResult, realResult);
	};
	
	@Test
	public void shouldBeTrue12() {
		String reference = "אַוָּזֵי";
		String billet = "אווזי";
		String expectedResult = "אַווָּזֵי";
		NikkudimSpreader spreader = new NikkudimSpreader(reference, billet);
		String realResult = spreader.getResult();
		assertEquals(expectedResult, realResult);
	};

	@Test
	public void shouldBeTrue13() {
		String reference = "אֲוִירוֹת";
		String billet = "אווירות";
		String expectedResult = "אֲווִירוֹת";
		NikkudimSpreader spreader = new NikkudimSpreader(reference, billet);
		String realResult = spreader.getResult();
		assertEquals(expectedResult, realResult);
	};

	@Test
	public void shouldBeTrue14() {
		String reference = "הִתְיַקְּרֻיּוֹת";
		String billet = "התייקרויות";
		String expectedResult = "הִתְייַקְּרוֹיּוֹת";
		NikkudimSpreader spreader = new NikkudimSpreader(reference, billet);
		String realResult = spreader.getResult();
		assertEquals(expectedResult, realResult);
	};


	@Test
	public void shouldBeTrue15() {
		String reference = "זָוִיּוֹת";
		String billet = "זוויות";
		String expectedResult = "זָווִיּוֹת";
		NikkudimSpreader spreader = new NikkudimSpreader(reference, billet);
		String realResult = spreader.getResult();
		assertEquals(expectedResult, realResult);
	};

	@Test
	public void shouldBeTrue16() {
		String reference = "חִוּוּט";
		String billet = "חיווט";
		String expectedResult = "חִיווּט";
		NikkudimSpreader spreader = new NikkudimSpreader(reference, billet);
		String realResult = spreader.getResult();
		assertEquals(expectedResult, realResult);
	};

	@Test
	public void shouldBeTrue17() {
		String reference = "סְתָו";
		String billet = "סתיו";
		String expectedResult = "סְתָיו";
		NikkudimSpreader spreader = new NikkudimSpreader(reference, billet);
		String realResult = spreader.getResult();
		assertEquals(expectedResult, realResult);
	};


	@Test
	public void shouldBeTrue18() {
		String reference = "סִיֹּמֶת";
		String billet = "סיומת";
		String expectedResult = "סִיוֹמֶת";
		NikkudimSpreader spreader = new NikkudimSpreader(reference, billet);
		String realResult = spreader.getResult();
		assertEquals(expectedResult, realResult);
	};
}
