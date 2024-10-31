# Hebrew Utils

## Description:
This is a side project for a Hebrew dictionary application for Android that will be available on Google Play once it's completed. The project consists of a package called `io.github.serafimcohen.hebrewutils` containing two utilities wrapped into Java classes.

### Cyrillizer
Cyrillizer transliterates Hebrew text with diacritics (nikkudim) to Cyrillic script. It can be used to make people who just started to learn Hebrew to understand pronunciation of Hebrew words correctly. For example: 

| Source: | Result: |
| --- | --- |
| אֵת כָּל עֲבֹדָתָם אֲשֶׁר עָבְדוּ בָהֶם בְּפָרֶך | эт каль аводатам ашэр авду ваhэм бэфарэх |
| וְנָתַתִּי אֶת־חֵן הָעָם־הַזֶּה בְּעֵינֵי מִצְרָיִם | вэнатати эт хэн hаам hазэ бээинэи мицраим |
| חֲתוּלָה  | хатуля |
| חֶשְׁבּוֹן אִינְפִינִיטֶסִימָלִי | хэшбон инфинитэcимали |
| טֶלֶסְקוֹפּ | тэлеcкоп |

To use it create an instance of Cyrillizer class and pass the string you need to cyrillize as a parameter. Then get a result of cyrillization using getResult() method:
    
	Cyrillizer cyrillizer = new Cyrillizer("מִתְּחִלָּה עוֹבְדֵי עֲבוֹדָה זָרָה הָיוּ אֲבוֹתֵינוּ וְעַכְשָׁיו קֵרְבָנוּ הַמָּקוֹם לַעֲבֹדָתוֹ");
	String result = cyrillizer.getResult();


### NikkudimSpreader
Since Hebrew words may be written without diacritics (nikkudim) most of the words can be written in two different forms. This class can "spread" diacritic symbols from the form with diacritics to the form without them to make it easily readable by beginners. For example:

| Form with diacritics: | Form without diacritics: | Result: |
| --- | --- | --- |
| אָזְנֵי | אוזני | אוֹזְנֵי |
| תִּסְמֹנֶת הָאַף הַלָּבָן | תסמונת האף הלבן | תִּסְמוֹנֶת הָאַף הַלָּבָן |
| אֲסֵפַתְכֶם | אסיפתכם | אֲסיפַתְכֶם |
| הִתְיַקְּרֻיּוֹת | התייקרויות | הִתְייַקְּרוֹיּוֹת |
| סְתָו | סתיו | סְתָיו |

To use it create an instance of NikkudimSpreader class and pass the strings with diacritics and without diacritics as parameters. Then get a result of spreading using getResult() method:
    
	NikkudimSpreader spreader = new NikkudimSpreader("רִגּוּל תַּעֲשִׂיָּתִי", "ריגול תעשייתי");
	String realResult = spreader.getResult();
