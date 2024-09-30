package io.github.serafimkogan.hebrewutils;

import java.util.List;

public class Symbols {
	public static final char hyphen = '-';
	public static final char space = ' ';
	public static final char dot = '.';
    public static final char apostrophe = '\'';
    public static final char next_line = '\n';
    

    public static class RegEx {
	    public static final String word_boundary = "\\b";
    }
    
    public static class Cyrillic {
    	public static final String a = "а";
    	public static final String b = "б";
    	public static final String v = "в";
    	public static final String g = "г";
    	public static final String d = "д";
    	public static final String ye = "е";
    	public static final String yo = "ё";
    	public static final String zh = "ж";
    	public static final String z = "з";
    	public static final String i = "и";
    	public static final String i_short = "й";
    	public static final String k = "к";
    	public static final String l = "л";
    	public static final String m = "м";
    	public static final String n = "н";
    	public static final String o = "о";
    	public static final String p = "п";
    	public static final String r = "р";
    	public static final String s = "с";
    	public static final String t = "т";
    	public static final String u = "у";
    	public static final String f = "ф";
    	public static final String kh = "х";
    	public static final String h = "h";
    	public static final String ts = "ц";
    	public static final String ch = "ч";
    	public static final String sh = "ш";
    	public static final String sch = "щ";
    	public static final String hard = "ъ";
    	public static final String yeru = "ы";
    	public static final String soft = "ь";
    	public static final String e = "э";
    	public static final String yu = "ю";
    	public static final String ya = "я";
    }
	
	public static class Hebrew {
		public static final String alef = "א";
		public static final String bet = "ב";
		public static final String gimel = "ג";
		public static final String dalet = "ד";
		public static final String hay = "ה";
		public static final String vav = "ו";
		public static final String zayin = "ז";
		public static final String khet = "ח";
		public static final String tet = "ט";
		public static final String yud = "י";
		public static final String kaf = "כ";
		public static final String kaf_sofit = "ך";
		public static final String lamed = "ל";
		public static final String mem = "מ";
		public static final String mem_sofit = "ם";
		public static final String nun = "נ";
		public static final String nun_sofit = "ן";
		public static final String sameh = "ס";
		public static final String ain = "ע";
		public static final String pey = "פ";
		public static final String pey_sofit = "ף";
		public static final String tsadi = "צ";
		public static final String tsadi_sofit = "ץ";
		public static final String kuf = "ק";
		public static final String resh = "ר";
		public static final String shin = "ש";
		public static final String tav = "ת";
		
		
		
	    public static final char etnahta = '֑';
	    public static final char qarney_para = '֟';
	    public static final char telisha_gedola = '֠';
	    
	    public static final char dagesh = 'ּ';
	    public static final char geresh = '׳';
	    public static final char sin_dot = 'ׂ';

	    public static final char holam = 'ֹ';
	    public static final char upper_dot_1 = 'ׄ'; 
	    public static final char upper_dot_2 = 'ֺ';
	    public static final char qamats = 'ָ';
	    public static final char segol = 'ֶ';
	    public static final char qamats_katan = 'ׇ';
	    public static final char hiriq = 'ִ';
	    public static final char sheva = 'ְ';
	    public static final char hataf_segol = 'ֱ';
	    public static final char hataf_patah = 'ֲ';
	    public static final char hataf_qamats = 'ֳ';
	    public static final char tsere = 'ֵ';
	    public static final char patah = 'ַ';
	    public static final char qubuts = 'ֻ';

	    public static final char meteg = 'ֽ';
	    public static final char shin_dot = 'ׁ';
	    public static final char zakef_katan = '֔';

	    public static final char abbreviation_gershayim = '״';
	    public static final char abbreviation_quotation_mark = '\"';
	    public static final String abbreviation_two_gereshs = "׳׳";
	    public static final String abbreviation_two_apostrophes = "''";

	    public static final char maqaf = '־';
	    public static final String vav_dagesh_vav = "וּו";
	    public static final String vav_vav = "וו";
	    public static final String yud_yud = "יי";
	    public static final String yud_vav = "יו";
	    
		public static boolean isHebrewDiacritics(char c) {
	    	return (c >= etnahta && c <= qarney_para
	    			|| c >= telisha_gedola && c <= qamats_katan)
	    			&& c != '־';
		}
		
		public static boolean isHebrewLetter(char c) {
			return c >= 'א' && c <= 'ײ';
	    }
		
		public static String removeDiacritics(String string) {
	        StringBuilder stringBuilder = new StringBuilder();

	        stringBuilder.append(string);
	        
	        for (int i = 0; i < stringBuilder.length(); i++) {
	        	if (stringBuilder.charAt(i) == geresh) {
	        		stringBuilder.setCharAt(i, apostrophe);
	        		continue;
	        	}
	        	if (stringBuilder.charAt(i) == abbreviation_gershayim) {
	        		stringBuilder.setCharAt(i, apostrophe);
	        		stringBuilder.insert(i + 1, apostrophe);
	        		i++;
	        		continue;
	        	}
	        	if ((stringBuilder.charAt(i) >= etnahta && stringBuilder.charAt(i) <= qarney_para
	        			|| stringBuilder.charAt(i) >= telisha_gedola && stringBuilder.charAt(i) <= qamats_katan)
	        			&& stringBuilder.charAt(i) != maqaf) {
	        		stringBuilder.deleteCharAt(i);
	        		i--;
	        		continue;
	        	}
	        }

	        return stringBuilder.toString();
	    }
		
		public static boolean nikkudimContainsNonStressedO(List<Character> nikkudim) {
			if (nikkudim.contains(Symbols.Hebrew.tsere)
					|| nikkudim.contains(Symbols.Hebrew.sheva)
					|| nikkudim.contains(Symbols.Hebrew.holam)
                    || nikkudim.contains(Symbols.Hebrew.upper_dot_1)
                    || nikkudim.contains(Symbols.Hebrew.upper_dot_2)
                    || nikkudim.contains(Symbols.Hebrew.qamats_katan)
                    || nikkudim.contains(Symbols.Hebrew.hataf_qamats)
                    || nikkudim.contains(Symbols.Hebrew.qubuts)
                    || nikkudim.contains(Symbols.Hebrew.qamats)
                    || nikkudim.contains(Symbols.Hebrew.hataf_patah)
                    || nikkudim.contains(Symbols.Hebrew.patah)
                    || nikkudim.contains(Symbols.Hebrew.segol)
                    || nikkudim.contains(Symbols.Hebrew.hataf_segol))
				return true;
			return false;
		}
	}
}
