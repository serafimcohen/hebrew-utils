# Hebrew Utils

## Description:
This is a side project for a Hebrew dictionary application for Android that will be available on Google Play once it's completed. The project consists of a package called `io.github.serafimkogan.hebrewutils` containing two utilities wrapped into Java classes.

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






