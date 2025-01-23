package io.github.serafimkogan.hebrewutils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class CyrillizerTest {

    @Test
    public void shouldThrowNullPointerException() {
		String source = null;
		assertThrows(NullPointerException.class, () -> new Cyrillizer(source));
    }

	@Test
	public void shouldCyrillizeCorrectly1() {
		String source = "רַבִּי עֲקִיבָא אוֹמֵר, שְׂחוֹק וְקַלּוּת רֹאשׁ, מַרְגִּילִין לְעֶרְוָה. מָסֹרֶת, סְיָג לַתּוֹרָה. מַעַשְׂרוֹת, סְיָג לָעשֶׁר. נְדָרִים, סְיָג לַפְּרִישׁוּת. סְיָג; לַחָכְמָה, שְׁתִיקָה";
		String expected = "раби акива омэр сэхок вэкалют рош маргилин леэрва. масорэт сэяг лятора. маасрот сэяг ляшэр. "
				+ "нэдарим сэяг ляпришут. сэяг ляхахма шэтика";
		Cyrillizer cyrillizer = new Cyrillizer(source);
		String actual = cyrillizer.getResult();
		assertEquals(expected, actual);
	}


	@Test
	public void shouldCyrillizeCorrectly2() {
		String source = "לְנֶפֶשׁ לֹא־יִטַּמָּא בְּעַמָּיו";
		String expected = "ленэфэш лё йитама бэамав";
		Cyrillizer cyrillizer = new Cyrillizer(source);
		String actual = cyrillizer.getResult();
		assertEquals(expected, actual);
	}

	@Test
	public void shouldCyrillizeCorrectly3() {
		String source = "אִם־יֵשׁ אֶת־נַפְשְׁכֶם לִקְבֹּר אֶת־מֵתִי מִלְּפָנַי";
		String expected = "им йэш эт нафшэхэм ликбор эт мэти милюфанаи";
		Cyrillizer cyrillizer = new Cyrillizer(source);
		String actual = cyrillizer.getResult();
		assertEquals(expected, actual);
	}

	@Test
	public void shouldCyrillizeCorrectly4() {
		String source = "נַפְשׁוֹ גֶּחָלִים תְּלַהֵט וְלַהַב מִפִּיו יֵצֵא׃";
		String expected = "нафшо гэхалим тэляhэт вэляhав мипиав йэцэ";
		Cyrillizer cyrillizer = new Cyrillizer(source);
		String actual = cyrillizer.getResult();
		assertEquals(expected, actual);
	}

	@Test
	public void shouldCyrillizeCorrectly5() {
		String source = "מִתְּחִלָּה עוֹבְדֵי עֲבוֹדָה זָרָה הָיוּ אֲבוֹתֵינוּ וְעַכְשָׁיו קֵרְבָנוּ הַמָּקוֹם לַעֲבֹדָתוֹ";
		String expected = "митхиля овдэи авода зара hаву авотэину вэахшав кэрвану hамаком ляаводато";
		Cyrillizer cyrillizer = new Cyrillizer(source);
		String actual = cyrillizer.getResult();
		assertEquals(expected, actual);
	}

	@Test
	public void shouldCyrillizeCorrectly6() {
		String source = "מִיַּלְדֵי הָעִבְרִים זֶה";
		String expected = "миалдэи hаиврим зэ";
		Cyrillizer cyrillizer = new Cyrillizer(source);
		String actual = cyrillizer.getResult();
		assertEquals(expected, actual);
	}

	@Test
	public void shouldCyrillizeCorrectly7() {
		String source = "מַה־טֹּבוּ אֹהָלֶיךָ יַעֲקֹב מִשְׁכְּנֹתֶיךָ יִשְׂרָאֵל׃";
		String expected = "ма тову оhалеиха йааков мишкэнотэиха йисраэль";
		Cyrillizer cyrillizer = new Cyrillizer(source);
		String actual = cyrillizer.getResult();
		assertEquals(expected, actual);
	}

	@Test
	public void shouldCyrillizeCorrectly8() {
		String source = "אֵת כָּל עֲבֹדָתָם אֲשֶׁר עָבְדוּ בָהֶם בְּפָרֶך";
		String expected = "эт каль аводатам ашэр авду ваhэм бэфарэх";
		Cyrillizer cyrillizer = new Cyrillizer(source);
		String actual = cyrillizer.getResult();
		assertEquals(expected, actual);
	}

	@Test
	public void shouldCyrillizeCorrectly9() {
		String source = "וַיְהִי מִקְנֵהוּ שִׁבְעַת אַלְפֵי צֹאן  וַעֲבֻדָּה רַבָּה מְאֹד וַיְהִי הָאִישׁ הַהוּא גָּדוֹל מִכָּל בְּנֵי קֶדֶם";
		String expected = "вайhи микнэhу шиват алфэи цон ваавода раба мэод вайhи hаиш hаhу гадоль микаль бэнэи кэдэм";
		Cyrillizer cyrillizer = new Cyrillizer(source);
		String actual = cyrillizer.getResult();
		assertEquals(expected, actual);
	}


	@Test
	public void shouldCyrillizeCorrectly10() {
		String source = "יְדַבֶּר נָא עַבְדְּךָ דָבָר בְּאָזְנֵי אֲדֹנִי וְאַל יִחַר אַפְּךָ בְּעַבְדֶּךָ";
		String expected = "йэдабэр на авдэха давар бэазнэи адони вэаль йихар апха бэавдэха";
		Cyrillizer cyrillizer = new Cyrillizer(source);
		String actual = cyrillizer.getResult();
		assertEquals(expected, actual);
	}

	@Test
	public void shouldCyrillizeCorrectly11() {
		String source = "וְנָתַתִּי אֶת־חֵן הָעָם־הַזֶּה בְּעֵינֵי מִצְרָיִם";
		String expected = "вэнатати эт хэн hаам hазэ бээинэи мицраим";
		Cyrillizer cyrillizer = new Cyrillizer(source);
		String actual = cyrillizer.getResult();
		assertEquals(expected, actual);
	}
}
