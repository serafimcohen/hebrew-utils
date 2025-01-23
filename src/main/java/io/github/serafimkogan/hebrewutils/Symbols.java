package io.github.serafimkogan.hebrewutils;

import java.util.Set;

public class Symbols {
	public static final char HYPHEN = '-';
	public static final char SPACE = ' ';
    public static final char APOSTROPHE = '\'';
    public static final char NEXT_LINE = '\n';
	public static final char DOT = '.';

    public static class RegEx {
	    public static final String WORD_BOUNDARY = "\\b";
	    public static final String ONE_OR_MORE_SPACES = " +";
    }
    
    public static class Cyrillic {
    	public static final String A = "а";
    	public static final String B = "б";
    	public static final String V = "в";
    	public static final String G = "г";
    	public static final String D = "д";
    	public static final String YE = "е";
    	public static final String YO = "ё";
    	public static final String ZH = "ж";
    	public static final String Z = "з";
    	public static final String I = "и";
    	public static final String I_SHORT = "й";
    	public static final String K = "к";
    	public static final String L = "л";
    	public static final String M = "м";
    	public static final String N = "н";
    	public static final String O = "о";
    	public static final String P = "п";
    	public static final String R = "р";
    	public static final String S = "с";
    	public static final String T = "т";
    	public static final String U = "у";
    	public static final String F = "ф";
    	public static final String KH = "х";
    	public static final String H = "h";
    	public static final String TS = "ц";
    	public static final String CH = "ч";
    	public static final String SH = "ш";
    	public static final String SCH = "щ";
    	public static final String HARD = "ъ";
    	public static final String YERU = "ы";
    	public static final String SOFT = "ь";
    	public static final String E = "э";
    	public static final String YU = "ю";
    	public static final String YA = "я";
    }
	
	public static class Hebrew {
		public static final String ALEF = "א";
		public static final String BET = "ב";
		public static final String GIMEL = "ג";
		public static final String DALET = "ד";
		public static final String HAY = "ה";
		public static final String VAV = "ו";
		public static final String ZAYIN = "ז";
		public static final String KHET = "ח";
		public static final String TET = "ט";
		public static final String YUD = "י";
		public static final String KAF = "כ";
		public static final String KAF_SOFIT = "ך";
		public static final String LAMED = "ל";
		public static final String MEM = "מ";
		public static final String MEM_SOFIT = "ם";
		public static final String NUN = "נ";
		public static final String NUN_SOFIT = "ן";
		public static final String SAMEH = "ס";
		public static final String AIN = "ע";
		public static final String PEY = "פ";
		public static final String PEY_SOFIT = "ף";
		public static final String TSADI = "צ";
		public static final String TSADI_SOFIT = "ץ";
		public static final String KUF = "ק";
		public static final String RESH = "ר";
		public static final String SHIN = "ש";
		public static final String TAV = "ת";
		
	    public static final char ETNAHTA = '֑';
	    public static final char QARNEY_PARA = '֟';
	    public static final char TELISHA_GEDOLA = '֠';
	    public static final char DAGESH = 'ּ';
	    public static final char GERESH = '׳';
	    public static final char SIN_DOT = 'ׂ';
	    public static final char HOLAM = 'ֹ';
	    public static final char UPPER_DOT_1 = 'ׄ'; 
	    public static final char UPPER_DOT_2 = 'ֺ';
	    public static final char QAMATS = 'ָ';
	    public static final char SEGOL = 'ֶ';
	    public static final char QAMATS_KATAN = 'ׇ';
	    public static final char HIRIQ = 'ִ';
	    public static final char SHEVA = 'ְ';
	    public static final char HATAF_SEGOL = 'ֱ';
	    public static final char HATAF_PATAH = 'ֲ';
	    public static final char HATAF_QAMATS = 'ֳ';
	    public static final char TSERE = 'ֵ';
	    public static final char PATAH = 'ַ';
	    public static final char QUBUTS = 'ֻ';
	    public static final char METEG = 'ֽ';
	    public static final char SHIN_DOT = 'ׁ';
	    public static final char ZAKEF_KATAN = '֔';
	    public static final char MAQAF = '־';

		public static final String DOT = ".";
	    public static final String ABBREVIATION_GERSHAYIM = "״";
	    public static final String ABBREVIATION_QUOTATION_MARK = "\"";
	    public static final String ABBREVIATION_TWO_GERESHS = "׳׳";
	    public static final String ABBREVIATION_TWO_APOSTROPHES = "''";

	    public static final String VAV_DAGESH_VAV = "וּו";
	    public static final String VAV_VAV = "וו";
	    public static final String YUD_YUD = "יי";
	    public static final String YUD_VAV = "יו";
	    
		public static boolean isHebrewDiacritics(char c) {
	    	return (c >= ETNAHTA && c <= QARNEY_PARA
	    			|| c >= TELISHA_GEDOLA && c <= QAMATS_KATAN)
	    			&& c != '־';
		}
		
		public static boolean isHebrewLetter(char c) {
			return c >= 'א' && c <= 'ײ';
	    }
		
		public static String removeDiacritics(String string) {
	        StringBuilder stringBuilder = new StringBuilder();

	        stringBuilder.append(string);
	        
	        for (int i = 0; i < stringBuilder.length(); i++) {
	        	if (stringBuilder.charAt(i) == GERESH) {
	        		stringBuilder.setCharAt(i, APOSTROPHE);
	        		continue;
	        	}
	        	if (String.valueOf(stringBuilder.charAt(i)).equals(ABBREVIATION_GERSHAYIM)) {
	        		stringBuilder.setCharAt(i, APOSTROPHE);
	        		stringBuilder.insert(i + 1, APOSTROPHE);
	        		i++;
	        		continue;
	        	}
	        	if ((stringBuilder.charAt(i) >= ETNAHTA && stringBuilder.charAt(i) <= QARNEY_PARA
	        			|| stringBuilder.charAt(i) >= TELISHA_GEDOLA && stringBuilder.charAt(i) <= QAMATS_KATAN)
	        			&& stringBuilder.charAt(i) != MAQAF) {
	        		stringBuilder.deleteCharAt(i);
	        		i--;
	        		continue;
	        	}
	        }

	        return stringBuilder.toString();
	    }

		public static boolean nikkudimContainsNonStressedO(Set<Character> nikkudim) {
			if (nikkudim.contains(Symbols.Hebrew.TSERE)
					|| nikkudim.contains(Symbols.Hebrew.SHEVA)
					|| nikkudim.contains(Symbols.Hebrew.HOLAM)
                    || nikkudim.contains(Symbols.Hebrew.UPPER_DOT_1)
                    || nikkudim.contains(Symbols.Hebrew.UPPER_DOT_2)
                    || nikkudim.contains(Symbols.Hebrew.QAMATS_KATAN)
                    || nikkudim.contains(Symbols.Hebrew.HATAF_QAMATS)
                    || nikkudim.contains(Symbols.Hebrew.QUBUTS)
                    || nikkudim.contains(Symbols.Hebrew.QAMATS)
                    || nikkudim.contains(Symbols.Hebrew.HATAF_PATAH)
                    || nikkudim.contains(Symbols.Hebrew.PATAH)
                    || nikkudim.contains(Symbols.Hebrew.SEGOL)
                    || nikkudim.contains(Symbols.Hebrew.HATAF_SEGOL))
				return true;
			return false;
		}
	}
}
